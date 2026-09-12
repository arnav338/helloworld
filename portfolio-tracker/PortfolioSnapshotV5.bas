Attribute VB_Name = "PortfolioSnapshotV5"
Option Explicit

Public Sub RecordPortfolioSnapshotV5()
    Dim dashboard As Worksheet
    Dim history As Worksheet
    Dim rowNumber As Long
    Dim sourceRow As Long
    Dim dailyCount As Long
    Dim outputColumn As Long
    Dim previousDay As Long
    Dim snapshotDay As Long

    On Error GoTo SnapshotError
    Set dashboard = ThisWorkbook.Worksheets("Dashboard")
    Set history = ThisWorkbook.Worksheets("Portfolio History")

    Application.ScreenUpdating = False
    Application.CalculateFull

    ' Preserve every snapshot in the 50-row history.
    history.Range("A7:L55").Copy Destination:=history.Range("A6")
    Application.CutCopyMode = False

    With history
        .Range("A55").Value = Now
        .Range("B55").Value = NumberOrZero(dashboard.Range("B4").Value)
        .Range("C55").Value = NumberOrZero(dashboard.Range("B5").Value)
        .Range("D55").Value = NumberOrZero(dashboard.Range("B6").Value)
        .Range("E55").Value = NumberOrZero(dashboard.Range("B8").Value)
        .Range("F55").Value = NumberOrZero(dashboard.Range("B9").Value)
        .Range("I55").Value = NumberOrZero(dashboard.Range("B12").Value)
        .Range("J55").Value = NumberOrZero(dashboard.Range("B13").Value)
        .Range("K55").Value = NumberOrZero(dashboard.Range("B14").Value)
        .Range("L55").Value = NumberOrZero(dashboard.Range("B15").Value)

        .Range("G6:H6").ClearContents
        For rowNumber = 7 To 55
            .Cells(rowNumber, "G").Formula = "=IF(OR(A" & rowNumber & "="""",A" & rowNumber - 1 & "="""",D" & rowNumber - 1 & "=""""),"""",D" & rowNumber & "-D" & rowNumber - 1 & ")"
            .Cells(rowNumber, "H").Formula = "=IF(OR(A" & rowNumber & "="""",A" & rowNumber - 1 & "="""",D" & rowNumber - 1 & "="""",D" & rowNumber - 1 & "=0),"""",G" & rowNumber & "/D" & rowNumber - 1 & ")"
        Next rowNumber
    End With

    ' These helpers retain the latest entry for each calendar day.
    ' The full history above still keeps every individual recording.
    dashboard.Range("AB2:BY3").ClearContents
    dashboard.Range("AB5:BY7").ClearContents
    dashboard.Range("AB10:BY11").ClearContents

    previousDay = 0
    dailyCount = 0

    For sourceRow = 6 To 55
        If IsDate(history.Cells(sourceRow, "A").Value) Then
            snapshotDay = CLng(Int(CDbl(CDate(history.Cells(sourceRow, "A").Value))))

            If snapshotDay <> previousDay Then
                dailyCount = dailyCount + 1
                outputColumn = 27 + dailyCount ' AB is column 28
                dashboard.Cells(2, outputColumn).Value = CDate(snapshotDay)
                dashboard.Cells(5, outputColumn).Value = CDate(snapshotDay)
                dashboard.Cells(10, outputColumn).Value = CDate(snapshotDay)
                previousDay = snapshotDay
            End If

            ' Later records on the same day replace only the chart point.
            outputColumn = 27 + dailyCount
            dashboard.Cells(3, outputColumn).Value = NumberOrZero(history.Cells(sourceRow, "L").Value)
            dashboard.Cells(6, outputColumn).Value = NumberOrZero(history.Cells(sourceRow, "J").Value)
            dashboard.Cells(7, outputColumn).Value = NumberOrZero(history.Cells(sourceRow, "I").Value)
            dashboard.Cells(11, outputColumn).Value = NumberOrZero(history.Cells(sourceRow, "D").Value)
        End If
    Next sourceRow

    dashboard.Range("AB2:BY2,AB5:BY5,AB10:BY10").NumberFormat = "dd-mmm-yy"
    dashboard.Range("AB3:BY3").NumberFormat = "0.0%"
    dashboard.Range("AB6:BY7,AB11:BY11").NumberFormat = "#,##0.00"

    If dailyCount > 0 Then
        UpdateOneSeriesChart dashboard, "Chart", "Net worth", dashboard.Range("AB10").Resize(1, dailyCount), dashboard.Range("AB11").Resize(1, dailyCount)
        UpdateOneSeriesChart dashboard, "InvestmentReturnHistoryChart", "Profit %", dashboard.Range("AB2").Resize(1, dailyCount), dashboard.Range("AB3").Resize(1, dailyCount)
        UpdateTwoSeriesChart dashboard, "CurrentVsInvestedPercentChart", dashboard.Range("AB5").Resize(1, dailyCount), dashboard.Range("AB6").Resize(1, dailyCount), dashboard.Range("AB7").Resize(1, dailyCount)
    End If

    UpdateProfitLabel dashboard
    Application.ScreenUpdating = True
    MsgBox "Portfolio snapshot recorded.", vbInformation, "Portfolio tracker"
    Exit Sub

SnapshotError:
    Application.ScreenUpdating = True
    MsgBox "Snapshot was not recorded. Error " & Err.Number & ": " & Err.Description, vbExclamation, "Portfolio tracker"
End Sub

Private Sub UpdateOneSeriesChart(ByVal dashboard As Worksheet, ByVal chartName As String, ByVal seriesName As String, ByVal categories As Range, ByVal values As Range)
    On Error Resume Next
    With dashboard.ChartObjects(chartName).Chart
        .SeriesCollection(1).Formula = "=SERIES(""" & seriesName & """," & categories.Address(True, True, xlA1, True) & "," & values.Address(True, True, xlA1, True) & ",1)"
        .HasLegend = False
    End With
    On Error GoTo 0
End Sub

Private Sub UpdateTwoSeriesChart(ByVal dashboard As Worksheet, ByVal chartName As String, ByVal categories As Range, ByVal invested As Range, ByVal currentValue As Range)
    On Error Resume Next
    With dashboard.ChartObjects(chartName).Chart
        .SeriesCollection(1).Formula = "=SERIES(""Invested amount""," & categories.Address(True, True, xlA1, True) & "," & invested.Address(True, True, xlA1, True) & ",1)"
        .SeriesCollection(2).Formula = "=SERIES(""Current value""," & categories.Address(True, True, xlA1, True) & "," & currentValue.Address(True, True, xlA1, True) & ",2)"
    End With
    On Error GoTo 0
End Sub

Private Sub UpdateProfitLabel(ByVal dashboard As Worksheet)
    Dim profitText As String

    profitText = "Profit: " & Format(NumberOrZero(dashboard.Range("B15").Value), "0.0%")
    On Error Resume Next
    dashboard.Shapes("ProfitLabelInside").TextFrame2.TextRange.Text = profitText
    dashboard.Shapes("ProfitLabelInside").TextFrame.Characters.Text = profitText
    On Error GoTo 0
End Sub

Private Function NumberOrZero(ByVal cellValue As Variant) As Double
    If Not IsError(cellValue) Then
        If IsNumeric(cellValue) Then NumberOrZero = CDbl(cellValue)
    End If
End Function
