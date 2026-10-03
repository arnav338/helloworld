@echo off
setlocal

rem Native Windows convenience launcher. ExecutionPolicy Bypass applies only to
rem this process; it does not modify the user's machine or profile policy.
powershell.exe -NoLogo -NoProfile -ExecutionPolicy Bypass -File "%~dp0start-local.ps1" %*
exit /b %ERRORLEVEL%
