Attribute VB_Name = "PortfolioSnapshotV4"
Option Explicit

Public Sub RecordPortfolioSnapshotV4()
    Dim dashboard As Worksheet
    Dim history As Worksheet
    Dim rowNumber As Long
    Dim chartColumn As Long
    Dim sourceRow As Long
    Dim previousDate As Date
    Dim snapshotDate As Date

    On Error GoTo SnapshotError
    Set dashboard = ThisWorkbook.Worksheets("Dashboard")
    Set history = ThisWorkbook.Worksheets("Portfolio History")

    Application.ScreenUpdating = False
    Application.Calculate

    history.Range("A7:L55").Copy Destination:=history.Range("A6")
    Application.CutCopyMode = False

    With history
        .Range("A55").Value = Now
        .Range("B55").Value = dashboard.Range("B4").Value
        .Range("C55").Value = dashboard.Range("B5").Value
        .Range("D55").Value = dashboard.Range("B6").Value
        .Range("E55").Value = dashboard.Range("B8").Value
        .Range("F55").Value = dashboard.Range("B9").Value
        .Range("I55").Value = dashboard.Range("B12").Value
        .Range("J55").Value = dashboard.Range("B13").Value
        .Range("K55").Value = dashboard.Range("B14").Value
        .Range("L55").Value = dashboard.Range("B15").Value

        .Range("G6:H6").ClearContents
        For rowNumber = 7 To 55
            .Cells(rowNumber, "G").Formula = "=IF(OR(A" & rowNumber & "="""",A" & rowNumber - 1 & "="""",D" & rowNumber - 1 & "=""""),"""",D" & rowNumber & "-D" & rowNumber - 1 & ")"
            .Cells(rowNumber, "H").Formula = "=IF(OR(A" & rowNumber & "="""",A" & rowNumber - 1 & "="""",D" & rowNumber - 1 & "="""",D" & rowNumber - 1 & "=0),"""",G" & rowNumber & "/D" & rowNumber - 1 & ")"
        Next rowNumber
    End With

    ' Net Worth chart: one point per day, using that day's latest snapshot.
    dashboard.Range("AB10:BY11").ClearContents
    chartColumn = 28
    previousDate = DateSerial(1900, 1, 1)

    For sourceRow = 6 To 55
        If IsDate(history.Cells(sourceRow, "A").Value) Then
            snapshotDate = DateValue(CDate(history.Cells(sourceRow, "A").Value))
            If snapshotDate <> previousDate Then
                dashboard.Cells(10, chartColumn).Value = snapshotDate
                chartColumn = chartColumn + 1
                previousDate = snapshotDate
            End If
            dashboard.Cells(11, chartColumn - 1).Value = history.Cells(sourceRow, "D").Value
        End If
    Next sourceRow

    dashboard.Range("AB10:BY10").NumberFormat = "dd-mmm-yy"
    dashboard.Range("AB11:BY11").NumberFormat = "#,##0.00"

    With dashboard.ChartObjects("Chart").Chart
        .SeriesCollection(1).Formula = "=SERIES(""Net worth"",Dashboard!$AB$10:$BY$10,Dashboard!$AB$11:$BY$11,1)"
        .HasLegend = False
    End With

    Application.ScreenUpdating = True
    MsgBox "Portfolio snapshot recorded.", vbInformation, "Portfolio tracker"
    Exit Sub

SnapshotError:
    Application.ScreenUpdating = True
    MsgBox "Snapshot was not recorded. Error " & Err.Number & ": " & Err.Description, vbExclamation, "Portfolio tracker"
End Sub
