# Portfolio Tracker

## Files

- `portfolio_tracker.xlsm` — the Excel tracker, dashboard, history, charts, and its current VBA macros.
- `PortfolioSnapshotV4.bas` — a backup of the VBA snapshot module. Keep this beside the workbook in case Excel removes or loses the macro module.

## Normal use

1. Open `portfolio_tracker.xlsm` in the desktop version of Microsoft Excel.
2. If Excel asks, enable macros.
3. On **Current Balances**:
   - For cash / emergency-fund rows, update only **Current value**.
   - For market-investment rows, set **Investment status** to `Market investment`, then update **Current value** and **Invested amount**. Gain/loss and Profit % calculate automatically.
4. On **Dashboard**, click **Record History** after each update.
5. Save the workbook (`Command + S` on Mac, `Ctrl + S` on Windows).

The workbook keeps up to 50 portfolio-history snapshots. The daily charts show only the latest entry for each date, so multiple recordings on the same day do not clutter them.

## Moving to a new computer

The workbook already contains the dashboard, formulas, charts, history, and VBA code. In most cases it will work immediately after copying this whole `portfolio-tracker` folder to the new computer.

You will need Microsoft Excel desktop. Excel for the web cannot run VBA macros. When you first open the workbook, enable macros and save it as an `.xlsm` file.

## If the Record History button does not work

1. Open the Visual Basic Editor:
   - Mac: **Tools → Macro → Visual Basic Editor**.
   - Windows: **Developer → Visual Basic** (or `Alt + F11`).
2. In the Project pane, choose the workbook project.
3. Select **File → Import File…** and choose `PortfolioSnapshotV4.bas`.
4. Return to Excel, right-click the Record History button, select **Assign Macro**, and select:
   `PortfolioSnapshotV4.RecordPortfolioSnapshotV4`
5. Save the workbook as **Excel Macro-Enabled Workbook (`.xlsm`)**.

If the Visual Basic project prompts for a password, do not try to modify existing protected code. Import the supplied module and assign the button to the imported macro.

## Backup

Back up `portfolio_tracker.xlsm` periodically. The history is stored inside the workbook, so a copy of this file preserves your balances and snapshots.
