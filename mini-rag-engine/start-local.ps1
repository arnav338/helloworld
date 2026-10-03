<#
.SYNOPSIS
    Defensive native-Windows startup for the Mini RAG Engine.

.DESCRIPTION
    This is the PowerShell equivalent of start-local.sh. It validates or safely
    repairs Java and Ollama, uses the pinned Maven Wrapper, validates both local
    model APIs, builds and tests every module, verifies the Spring Boot JAR,
    starts the application, and refuses to report success until health checks
    pass.

    Compatibility target: Windows 10/11 with Windows PowerShell 5.1 or newer.
    The Java application is cross-platform; this file replaces Unix-only shell,
    Homebrew, lsof, nohup, signal, and path behavior with Windows equivalents.

    Safety rules:
      * Never deletes a SQLite database or an Ollama model.
      * Never kills an unknown process merely because it owns a required port.
      * Uses winget only when a required tool is missing/broken and installation
        has not been disabled with -NoInstall.
      * Re-pulls a model only if missing or its real API smoke test fails.
#>

[CmdletBinding()]
param(
    [switch]$Background,
    [switch]$CheckOnly,
    [switch]$NoInstall,
    [switch]$Help
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$Script:ProjectRoot = $PSScriptRoot
$Script:RunDirectory = Join-Path $ProjectRoot '.run'
$Script:LockDirectory = Join-Path $RunDirectory 'startup-windows.lock'
$Script:ApplicationLog = Join-Path $RunDirectory 'application-windows.log'
$Script:ApplicationErrorLog = Join-Path $RunDirectory 'application-windows-error.log'
$Script:OllamaLog = Join-Path $RunDirectory 'ollama-windows.log'
$Script:OllamaErrorLog = Join-Path $RunDirectory 'ollama-windows-error.log'
$Script:ApplicationPidFile = Join-Path $RunDirectory 'application-windows.pid'
$Script:OllamaPidFile = Join-Path $RunDirectory 'ollama-windows.pid'
$Script:JarPath = Join-Path $ProjectRoot 'rag-application\target\rag-application-0.1.0-SNAPSHOT.jar'
$Script:MavenWrapper = Join-Path $ProjectRoot 'mvnw.cmd'
$Script:ApplicationProcess = $null
$Script:StartedApplication = $false
$Script:LockAcquired = $false
$Script:OllamaCommand = $null
$Script:JavaCommand = $null

function Write-StartupLog {
    param([Parameter(Mandatory = $true)][string]$Message)
    Write-Host "[startup] $Message"
}

function Write-StartupWarning {
    param([Parameter(Mandatory = $true)][string]$Message)
    Write-Warning "[startup] $Message"
}

function Show-Usage {
    @'
Usage from PowerShell:
  .\start-local.ps1 [-Background] [-CheckOnly] [-NoInstall]

Usage from Command Prompt or PowerShell without changing execution policy:
  start-local.cmd [options]

Default behavior:
  Repair/check prerequisites, verify local Ollama models, run a clean Maven
  package, start Spring Boot, verify /actuator/health, and remain attached until
  Ctrl+C is pressed.

Options:
  -Background   Start Spring Boot, verify health, then return to the shell.
  -CheckOnly    Diagnose/repair models and build; do not start Spring Boot.
  -NoInstall    Diagnose missing/broken tools but do not invoke winget.
  -Help         Show this help text.

Common PowerShell overrides:
  $env:RAG_CHAT_MODEL = 'another-chat-model'
  $env:RAG_EMBEDDING_MODEL = 'another-embedding-model'
  $env:SERVER_PORT = '8081'
  .\start-local.ps1

Logs and PID files are written beneath .run\ and are ignored by Git.
'@ | Write-Host
}

if ($Help) {
    Show-Usage
    exit 0
}

function Get-EnvironmentOrDefault {
    param(
        [Parameter(Mandatory = $true)][string]$Name,
        [Parameter(Mandatory = $true)][string]$DefaultValue
    )
    $value = [Environment]::GetEnvironmentVariable($Name, 'Process')
    if ([string]::IsNullOrWhiteSpace($value)) { return $DefaultValue }
    return $value
}

# Keep these defaults synchronized with application.yml and start-local.sh.
$env:RAG_CHAT_MODEL = Get-EnvironmentOrDefault 'RAG_CHAT_MODEL' 'llama2'
$env:RAG_EMBEDDING_MODEL = Get-EnvironmentOrDefault 'RAG_EMBEDDING_MODEL' 'embeddinggemma'
$env:RAG_CHAT_BASE_URL = Get-EnvironmentOrDefault 'RAG_CHAT_BASE_URL' 'http://127.0.0.1:11434/v1'
$env:RAG_EMBEDDING_BASE_URL = Get-EnvironmentOrDefault 'RAG_EMBEDDING_BASE_URL' 'http://127.0.0.1:11434/v1'
$env:SERVER_PORT = Get-EnvironmentOrDefault 'SERVER_PORT' '8080'
$Script:OllamaOrigin = Get-EnvironmentOrDefault 'OLLAMA_ORIGIN' 'http://127.0.0.1:11434'
$Script:StartupTimeoutSeconds = [int](Get-EnvironmentOrDefault 'STARTUP_TIMEOUT_SECONDS' '90')
$Script:ModelTimeoutSeconds = [int](Get-EnvironmentOrDefault 'MODEL_TIMEOUT_SECONDS' '240')

function Test-CommandAvailable {
    param([Parameter(Mandatory = $true)][string]$Name)
    return $null -ne (Get-Command $Name -ErrorAction SilentlyContinue)
}

function Refresh-ProcessPath {
    # Installers update persistent environment variables, but the current
    # PowerShell process does not automatically receive those changes.
    $machinePath = [Environment]::GetEnvironmentVariable('Path', 'Machine')
    $userPath = [Environment]::GetEnvironmentVariable('Path', 'User')
    $env:Path = "$machinePath;$userPath"
}

function Assert-WingetAvailable {
    param([Parameter(Mandatory = $true)][string]$Purpose)
    if ($NoInstall) {
        throw "$Purpose is missing/broken and -NoInstall was requested."
    }
    if (-not (Test-CommandAvailable 'winget.exe')) {
        throw "$Purpose is missing/broken and winget is unavailable. Install Microsoft App Installer or install $Purpose manually."
    }
}

function Invoke-WingetRepair {
    param(
        [Parameter(Mandatory = $true)][string]$PackageId,
        [Parameter(Mandatory = $true)][string]$DisplayName
    )
    Assert-WingetAvailable $DisplayName
    Write-StartupLog "Installing or repairing $DisplayName through winget package '$PackageId'"
    # --force repairs an existing package on current WinGet releases. Some
    # older Windows 10 App Installer releases do not recognize that argument,
    # so retry with their compatible argument set before failing.
    & winget.exe install --id $PackageId --exact --silent --force `
        --accept-package-agreements --accept-source-agreements
    $wingetExitCode = $LASTEXITCODE
    if ($wingetExitCode -ne 0) {
        Write-StartupWarning "WinGet repair returned $wingetExitCode; retrying with the older-compatible argument set."
        & winget.exe install --id $PackageId --exact --silent `
            --accept-package-agreements --accept-source-agreements
        $wingetExitCode = $LASTEXITCODE
    }
    if ($wingetExitCode -ne 0) {
        throw "winget could not install/repair $DisplayName (exit code $wingetExitCode)."
    }
    Refresh-ProcessPath
}

function Get-JavaMajorVersion {
    param([Parameter(Mandatory = $true)][string]$JavaExecutable)
    $versionText = (& $JavaExecutable -version 2>&1 | Out-String)
    if ($versionText -match 'version\s+"(?<major>\d+)') {
        return [int]$Matches.major
    }
    return 0
}

function Find-Java21 {
    $java = Get-Command 'java.exe' -ErrorAction SilentlyContinue
    if ($null -ne $java -and (Get-JavaMajorVersion $java.Source) -ge 21) {
        return $java.Source
    }

    # A JDK may have been installed successfully but not yet added to this
    # process's PATH. Search the standard Temurin/Java installation locations.
    $candidateRoots = @(
        (Join-Path $env:ProgramFiles 'Eclipse Adoptium'),
        (Join-Path $env:ProgramFiles 'Java')
    )
    foreach ($root in $candidateRoots) {
        if (-not (Test-Path $root)) { continue }
        $candidate = Get-ChildItem -Path $root -Directory -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like 'jdk-21*' } |
            Sort-Object Name -Descending |
            Select-Object -First 1
        if ($null -eq $candidate) { continue }
        $javaPath = Join-Path $candidate.FullName 'bin\java.exe'
        if ((Test-Path $javaPath) -and (Get-JavaMajorVersion $javaPath) -ge 21) {
            $env:JAVA_HOME = $candidate.FullName
            $env:Path = "$(Join-Path $candidate.FullName 'bin');$env:Path"
            return $javaPath
        }
    }
    return $null
}

function Ensure-Java21 {
    Refresh-ProcessPath
    $Script:JavaCommand = Find-Java21
    if ($null -ne $JavaCommand) {
        $version = (& $JavaCommand -version 2>&1 | Select-Object -First 1)
        Write-StartupLog "Java $version"
        return
    }

    Invoke-WingetRepair 'EclipseAdoptium.Temurin.21.JDK' 'Java 21 JDK'
    $Script:JavaCommand = Find-Java21
    if ($null -eq $JavaCommand) {
        throw 'Java repair completed, but a working Java 21+ JDK could not be found.'
    }
    Write-StartupLog "Java repaired: $(& $JavaCommand -version 2>&1 | Select-Object -First 1)"
}

function Find-Ollama {
    $command = Get-Command 'ollama.exe' -ErrorAction SilentlyContinue
    if ($null -ne $command) { return $command.Source }

    $standardPath = Join-Path $env:LOCALAPPDATA 'Programs\Ollama\ollama.exe'
    if (Test-Path $standardPath) {
        $env:Path = "$(Split-Path $standardPath -Parent);$env:Path"
        return $standardPath
    }
    return $null
}

function Test-OllamaCli {
    param([AllowNull()][string]$Executable)
    if ([string]::IsNullOrWhiteSpace($Executable) -or -not (Test-Path $Executable)) { return $false }
    & $Executable --version *> $null
    return $LASTEXITCODE -eq 0
}

function Ensure-OllamaCli {
    Refresh-ProcessPath
    $Script:OllamaCommand = Find-Ollama
    if (Test-OllamaCli $OllamaCommand) {
        Write-StartupLog ((& $OllamaCommand --version 2>&1 | Select-Object -First 1).ToString())
        return
    }

    Invoke-WingetRepair 'Ollama.Ollama' 'Ollama'
    $Script:OllamaCommand = Find-Ollama
    if (-not (Test-OllamaCli $OllamaCommand)) {
        throw 'Ollama repair completed, but its CLI still does not work.'
    }
    Write-StartupLog "Ollama repaired: $(& $OllamaCommand --version 2>&1 | Select-Object -First 1)"
}

function Invoke-LocalRestRequest {
    param(
        [Parameter(Mandatory = $true)][string]$Uri,
        [ValidateSet('Get', 'Post')][string]$Method = 'Get',
        [AllowNull()][object]$Body = $null,
        [int]$TimeoutSeconds = 3
    )
    $parameters = @{
        Uri = $Uri
        Method = $Method
        TimeoutSec = $TimeoutSeconds
        ErrorAction = 'Stop'
    }
    if ($null -ne $Body) {
        $parameters.ContentType = 'application/json'
        $parameters.Body = ($Body | ConvertTo-Json -Depth 8 -Compress)
    }
    return Invoke-RestMethod @parameters
}

function Test-UrlResponds {
    param([Parameter(Mandatory = $true)][string]$Uri)
    try {
        $null = Invoke-LocalRestRequest -Uri $Uri -TimeoutSeconds 3
        return $true
    }
    catch { return $false }
}

function Get-PortOwnerDescription {
    param([Parameter(Mandatory = $true)][int]$Port)
    try {
        # @() guarantees an array even for one listener. That avoids scalar
        # collection differences in Windows PowerShell 5.1 under StrictMode.
        $listeners = @(Get-NetTCPConnection -State Listen -LocalPort $Port -ErrorAction Stop)
        foreach ($listener in $listeners) {
            $process = Get-Process -Id $listener.OwningProcess -ErrorAction SilentlyContinue
            if ($null -ne $process) {
                Write-StartupWarning "Port $Port is owned by PID $($process.Id) ($($process.ProcessName))."
            }
            else {
                Write-StartupWarning "Port $Port is owned by PID $($listener.OwningProcess)."
            }
        }
        return $listeners.Count -gt 0
    }
    catch { return $false }
}

function Wait-ForUrl {
    param(
        [Parameter(Mandatory = $true)][string]$Uri,
        [Parameter(Mandatory = $true)][int]$TimeoutSeconds,
        [Parameter(Mandatory = $true)][string]$Label
    )
    $stopwatch = [Diagnostics.Stopwatch]::StartNew()
    while ($stopwatch.Elapsed.TotalSeconds -lt $TimeoutSeconds) {
        if (Test-UrlResponds $Uri) { return }
        Start-Sleep -Seconds 1
    }
    throw "Timed out after ${TimeoutSeconds}s waiting for $Label at $Uri."
}

function Ensure-OllamaServer {
    $versionUri = "$($OllamaOrigin.TrimEnd('/'))/api/version"
    if (Test-UrlResponds $versionUri) {
        Write-StartupLog "Ollama server is ready at $OllamaOrigin"
        return
    }

    if (Get-PortOwnerDescription 11434) {
        throw 'Port 11434 is occupied, but it is not answering as Ollama. Stop that process or configure Ollama correctly.'
    }

    Write-StartupLog 'Starting the local Ollama server'
    New-Item -ItemType File -Path $OllamaLog -Force | Out-Null
    New-Item -ItemType File -Path $OllamaErrorLog -Force | Out-Null
    $process = Start-Process -FilePath $OllamaCommand -ArgumentList 'serve' -PassThru `
        -WindowStyle Hidden -RedirectStandardOutput $OllamaLog -RedirectStandardError $OllamaErrorLog
    Set-Content -Path $OllamaPidFile -Value $process.Id -Encoding ASCII
    try {
        Wait-ForUrl -Uri $versionUri -TimeoutSeconds 60 -Label 'Ollama'
    }
    catch {
        Get-Content $OllamaLog -Tail 80 -ErrorAction SilentlyContinue | Write-Warning
        Get-Content $OllamaErrorLog -Tail 80 -ErrorAction SilentlyContinue | Write-Warning
        throw
    }
    Write-StartupLog "Ollama started with PID $($process.Id)"
}

function Test-ModelInstalled {
    param([Parameter(Mandatory = $true)][string]$Model)
    & $OllamaCommand show $Model *> $null
    return $LASTEXITCODE -eq 0
}

function Repair-Model {
    param(
        [Parameter(Mandatory = $true)][string]$Model,
        [Parameter(Mandatory = $true)][string]$Purpose
    )
    Write-StartupLog "Pulling/repairing $Purpose model '$Model' through Ollama"
    & $OllamaCommand pull $Model
    if ($LASTEXITCODE -ne 0 -or -not (Test-ModelInstalled $Model)) {
        throw "Ollama could not pull/verify model '$Model'. Check network, disk space, and model name."
    }
}

function Ensure-ModelInstalled {
    param(
        [Parameter(Mandatory = $true)][string]$Model,
        [Parameter(Mandatory = $true)][string]$Purpose
    )
    if (Test-ModelInstalled $Model) {
        Write-StartupLog "$Purpose model '$Model' is already installed; skipping download"
    }
    else {
        Repair-Model -Model $Model -Purpose $Purpose
    }
}

function Test-EmbeddingApi {
    $body = @{ model = $env:RAG_EMBEDDING_MODEL; input = @('local startup health check') }
    try {
        $response = Invoke-LocalRestRequest -Uri "$($env:RAG_EMBEDDING_BASE_URL.TrimEnd('/'))/embeddings" `
            -Method Post -Body $body -TimeoutSeconds $ModelTimeoutSeconds
        return $null -ne $response.data -and $response.data.Count -gt 0 `
            -and $null -ne $response.data[0].embedding -and $response.data[0].embedding.Count -gt 0
    }
    catch {
        Write-StartupWarning "Embedding API error: $($_.Exception.Message)"
        return $false
    }
}

function Test-ChatApi {
    $body = @{
        model = $env:RAG_CHAT_MODEL
        messages = @(@{ role = 'user'; content = 'Reply with ready' })
        temperature = 0
    }
    try {
        $response = Invoke-LocalRestRequest -Uri "$($env:RAG_CHAT_BASE_URL.TrimEnd('/'))/chat/completions" `
            -Method Post -Body $body -TimeoutSeconds $ModelTimeoutSeconds
        return $null -ne $response.choices -and $response.choices.Count -gt 0 `
            -and -not [string]::IsNullOrWhiteSpace($response.choices[0].message.content)
    }
    catch {
        Write-StartupWarning "Chat API error: $($_.Exception.Message)"
        return $false
    }
}

function Verify-ModelApis {
    Write-StartupLog "Testing embedding model '$($env:RAG_EMBEDDING_MODEL)' through the real API"
    if (-not (Test-EmbeddingApi)) {
        Write-StartupWarning 'Embedding smoke test failed; attempting one safe model repair.'
        Repair-Model -Model $env:RAG_EMBEDDING_MODEL -Purpose 'embedding'
        if (-not (Test-EmbeddingApi)) { throw 'Embedding API still fails after model repair.' }
    }

    Write-StartupLog "Testing chat model '$($env:RAG_CHAT_MODEL)' through the real API"
    if (-not (Test-ChatApi)) {
        Write-StartupWarning 'Chat smoke test failed; attempting one safe model repair.'
        Repair-Model -Model $env:RAG_CHAT_MODEL -Purpose 'chat'
        if (-not (Test-ChatApi)) { throw 'Chat API still fails after model repair.' }
    }
    Write-StartupLog 'Both local model APIs passed'
}

function Assert-LocalConfiguration {
    if ($env:RAG_CHAT_MODEL -notmatch '^[A-Za-z0-9._:/-]+$') {
        throw "RAG_CHAT_MODEL contains unsupported characters: $($env:RAG_CHAT_MODEL)"
    }
    if ($env:RAG_EMBEDDING_MODEL -notmatch '^[A-Za-z0-9._:/-]+$') {
        throw "RAG_EMBEDDING_MODEL contains unsupported characters: $($env:RAG_EMBEDDING_MODEL)"
    }

    $allowedBases = @(
        "$($OllamaOrigin.TrimEnd('/'))/v1",
        'http://127.0.0.1:11434/v1',
        'http://localhost:11434/v1'
    )
    if ($env:RAG_CHAT_BASE_URL -notin $allowedBases) {
        throw "start-local.ps1 manages local Ollama only; RAG_CHAT_BASE_URL is $($env:RAG_CHAT_BASE_URL)."
    }
    if ($env:RAG_EMBEDDING_BASE_URL -notin $allowedBases) {
        throw "start-local.ps1 manages local Ollama only; RAG_EMBEDDING_BASE_URL is $($env:RAG_EMBEDDING_BASE_URL)."
    }
    $parsedPort = 0
    if (-not [int]::TryParse($env:SERVER_PORT, [ref]$parsedPort) -or $parsedPort -lt 1 -or $parsedPort -gt 65535) {
        throw 'SERVER_PORT must be an integer between 1 and 65535.'
    }
}

function Test-FreeDiskSpace {
    $root = [IO.Path]::GetPathRoot($ProjectRoot)
    $drive = New-Object IO.DriveInfo($root)
    if ($drive.AvailableFreeSpace -lt 5GB) {
        Write-StartupWarning "Less than 5 GiB is free on $root. Maven/model downloads may fail."
    }
}

function Assert-ProjectLayout {
    $required = @(
        'pom.xml',
        'mvnw.cmd',
        '.mvn\wrapper\maven-wrapper.properties',
        'rag-application\pom.xml',
        'rag-application\src\main\resources\application.yml'
    )
    foreach ($relativePath in $required) {
        if (-not (Test-Path (Join-Path $ProjectRoot $relativePath))) {
            throw "Project is incomplete: missing $relativePath."
        }
    }
}

function Invoke-MavenBuild {
    param([switch]$ForceUpdates)
    $arguments = @('clean', 'package')
    if ($ForceUpdates) { $arguments = @('-U') + $arguments }

    Push-Location $ProjectRoot
    try {
        & $MavenWrapper @arguments
        return $LASTEXITCODE
    }
    finally { Pop-Location }
}

function Build-AndVerifyJar {
    Write-StartupLog 'Running pinned Maven Wrapper clean build and all tests'
    $exitCode = Invoke-MavenBuild
    if ($exitCode -ne 0) {
        Write-StartupWarning 'Normal Maven build failed; retrying once with dependency metadata refresh (-U).'
        $exitCode = Invoke-MavenBuild -ForceUpdates
    }
    if ($exitCode -ne 0) { throw "Maven build failed with exit code $exitCode." }
    if (-not (Test-Path $JarPath) -or (Get-Item $JarPath).Length -eq 0) {
        throw "Build succeeded but executable JAR is missing: $JarPath"
    }

    # Do not assume JAVA_HOME exists. A perfectly valid Windows JDK may be on
    # PATH without defining it, so find jar.exe beside the java.exe that this
    # script already validated. This also prevents mixing tools from two JDKs.
    $jarTool = Join-Path (Split-Path $JavaCommand -Parent) 'jar.exe'
    if (-not (Test-Path $jarTool)) {
        $jarCommand = Get-Command 'jar.exe' -ErrorAction SilentlyContinue
        if ($null -eq $jarCommand) { throw 'The JDK jar tool is unavailable.' }
        $jarTool = $jarCommand.Source
    }
    $listing = & $jarTool tf $JarPath
    if ($LASTEXITCODE -ne 0) { throw 'The executable JAR could not be read.' }
    if (-not ($listing -match '^BOOT-INF/')) { throw 'JAR is not a Spring Boot executable archive.' }
    if (-not ($listing -match 'dev/learning/rag/app/MiniRagApplication.class')) {
        throw 'JAR does not contain MiniRagApplication.'
    }
    Write-StartupLog "Executable JAR verified: $JarPath"
}

function Get-ApplicationHealthUri {
    return "http://127.0.0.1:$($env:SERVER_PORT)/actuator/health"
}

function Test-ApplicationHealthy {
    try {
        $response = Invoke-LocalRestRequest -Uri (Get-ApplicationHealthUri) -TimeoutSeconds 3
        return $response.status -eq 'UP'
    }
    catch { return $false }
}

function Assert-ApplicationPortAvailable {
    if (Test-ApplicationHealthy) {
        Write-StartupLog "A healthy application is already running on port $($env:SERVER_PORT); no duplicate will be started"
        return $false
    }
    if (Get-PortOwnerDescription ([int]$env:SERVER_PORT)) {
        throw "Port $($env:SERVER_PORT) is occupied. Choose another port, for example: `$env:SERVER_PORT='8081'; .\start-local.ps1"
    }
    return $true
}

function Wait-ForApplication {
    $stopwatch = [Diagnostics.Stopwatch]::StartNew()
    while ($stopwatch.Elapsed.TotalSeconds -lt $StartupTimeoutSeconds) {
        if (Test-ApplicationHealthy) { return }
        if ($null -ne $ApplicationProcess -and $ApplicationProcess.HasExited) {
            Get-Content $ApplicationLog -Tail 120 -ErrorAction SilentlyContinue | Write-Warning
            Get-Content $ApplicationErrorLog -Tail 120 -ErrorAction SilentlyContinue | Write-Warning
            throw 'Spring Boot exited before becoming healthy.'
        }
        Start-Sleep -Seconds 1
        if ($null -ne $ApplicationProcess) { $ApplicationProcess.Refresh() }
    }
    throw "Spring Boot did not become healthy within ${StartupTimeoutSeconds}s."
}

function Start-Application {
    New-Item -ItemType File -Path $ApplicationLog -Force | Out-Null
    New-Item -ItemType File -Path $ApplicationErrorLog -Force | Out-Null
    $arguments = @('-jar', ('"{0}"' -f $JarPath))
    Write-StartupLog 'Starting Spring Boot'
    $Script:ApplicationProcess = Start-Process -FilePath $JavaCommand -ArgumentList $arguments `
        -WorkingDirectory $ProjectRoot -PassThru -WindowStyle Hidden `
        -RedirectStandardOutput $ApplicationLog -RedirectStandardError $ApplicationErrorLog
    $Script:StartedApplication = $true
    Set-Content -Path $ApplicationPidFile -Value $ApplicationProcess.Id -Encoding ASCII

    Wait-ForApplication
    Start-Sleep -Seconds 3
    $ApplicationProcess.Refresh()
    if ($ApplicationProcess.HasExited) { throw 'Application exited during the post-start stability check.' }
    if (-not (Test-ApplicationHealthy)) { throw 'Application health regressed during the stability check.' }

    Write-StartupLog 'SUCCESS: prerequisites, model APIs, build checks, and Spring health checks passed'
    Write-StartupLog "Application: http://127.0.0.1:$($env:SERVER_PORT)"
    Write-StartupLog "Health:      $(Get-ApplicationHealthUri)"
    Write-StartupLog "PID:         $($ApplicationProcess.Id)"
    Write-StartupLog "Log:         $ApplicationLog"
    Write-StartupLog "Error log:   $ApplicationErrorLog"

    if (-not $Background) {
        Write-StartupLog 'Attached mode is active. Press Ctrl+C to stop Spring Boot.'
        Get-Content $ApplicationLog -Tail 40 -ErrorAction SilentlyContinue
        while (-not $ApplicationProcess.HasExited) {
            Start-Sleep -Seconds 1
            $ApplicationProcess.Refresh()
        }
        throw "Spring Boot exited unexpectedly with code $($ApplicationProcess.ExitCode)."
    }
}

function Acquire-StartupLock {
    New-Item -ItemType Directory -Path $RunDirectory -Force | Out-Null
    try {
        New-Item -ItemType Directory -Path $LockDirectory -ErrorAction Stop | Out-Null
    }
    catch {
        $ownerFile = Join-Path $LockDirectory 'pid'
        $owner = if (Test-Path $ownerFile) { Get-Content $ownerFile -ErrorAction SilentlyContinue } else { $null }
        if ($null -ne $owner -and (Get-Process -Id ([int]$owner) -ErrorAction SilentlyContinue)) {
            throw "Another startup script is running with PID $owner."
        }
        Write-StartupWarning 'Recovering a stale Windows startup lock.'
        Remove-Item $LockDirectory -Recurse -Force -ErrorAction Stop
        New-Item -ItemType Directory -Path $LockDirectory -ErrorAction Stop | Out-Null
    }
    Set-Content -Path (Join-Path $LockDirectory 'pid') -Value $PID -Encoding ASCII
    $Script:LockAcquired = $true
}

function Release-StartupLock {
    if ($LockAcquired -and (Test-Path $LockDirectory)) {
        Remove-Item $LockDirectory -Recurse -Force -ErrorAction SilentlyContinue
    }
}

try {
    Write-StartupLog 'Mini RAG Engine native Windows startup diagnosis'
    Write-StartupLog "Project: $ProjectRoot"
    Acquire-StartupLock
    Assert-LocalConfiguration
    Test-FreeDiskSpace
    Ensure-Java21
    Ensure-OllamaCli
    Ensure-OllamaServer
    Ensure-ModelInstalled -Model $env:RAG_EMBEDDING_MODEL -Purpose 'embedding'
    Ensure-ModelInstalled -Model $env:RAG_CHAT_MODEL -Purpose 'chat'
    Verify-ModelApis
    Assert-ProjectLayout
    Build-AndVerifyJar

    if ($CheckOnly) {
        Write-StartupLog 'SUCCESS: prerequisites, model APIs, tests, and executable JAR are ready'
        exit 0
    }

    if (Assert-ApplicationPortAvailable) {
        Start-Application
    }
    else {
        Write-StartupLog 'SUCCESS: project diagnostics passed and the existing application is healthy'
    }
}
catch {
    Write-Error "[startup] ERROR: $($_.Exception.Message)"
    Write-Error "[startup] Application log: $ApplicationLog"
    Write-Error "[startup] Application error log: $ApplicationErrorLog"
    Write-Error "[startup] Ollama log: $OllamaLog"
    Write-Error "[startup] Ollama error log: $OllamaErrorLog"
    exit 1
}
finally {
    # Stop only the foreground Spring process started by this invocation.
    # Ollama stays running because it is a shared local service.
    if ($StartedApplication -and -not $Background -and $null -ne $ApplicationProcess) {
        $ApplicationProcess.Refresh()
        if (-not $ApplicationProcess.HasExited) {
            Write-StartupLog "Stopping Spring Boot process $($ApplicationProcess.Id)"
            Stop-Process -Id $ApplicationProcess.Id -ErrorAction SilentlyContinue
            $ApplicationProcess.WaitForExit(10000) | Out-Null
        }
    }
    Release-StartupLock
}
