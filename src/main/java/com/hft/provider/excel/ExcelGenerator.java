package com.hft.provider.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of an extended Generator.generate() method:
 * <ol>
 *     <li>Create a {@link Sheet} with {@link ExcelGenerator#createSheet(String)}</li>
 *     <li>Write all rows with {@link Sheet#createRow(int)}</li>
 *     <li>Write the header row with {@link ExcelGenerator#writeHeaderCell(Row, int, String)} (row 0)</li>
 *     <li>Write all cells with {@link ExcelGenerator#writeRowCell(Row, int, String)} (row > 0)</li>
 *     <li>Finish the generation with {@link ExcelGenerator#exportWorkbook()}</li>
 * </ol>
 * Note: write a method for with writeRow(Row) for header and content. Use a counter++ for the column indexes.
 */
public abstract class ExcelGenerator {
    private final XSSFWorkbook workbook;
    private final CellStyle headerStyle;
    private final CellStyle textStyle;
    private final CellStyle dateStyle;

    /**
     * Holds the Excel workbook to work on.
     */
    protected ExcelGenerator() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = createHeaderStyle();
        this.textStyle = createRowStyle();
        this.dateStyle = createDateStyle();
    }

    /**
     * Creates a new sheet in the workbook.
     *
     * @param sheetName name
     * @return {@link Sheet} to work on
     */
    protected Sheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * Write the workbook into a temporary file.
     *
     * @return Temporary {@link File} to send via resources.
     * @throws IOException from the {@link FileOutputStream}
     */
    protected File exportWorkbook() throws IOException {
        File file = File.createTempFile("excel-export", ".xslx");
        try (FileOutputStream stream = new FileOutputStream(file)) {
            workbook.write(stream);
            workbook.close();
        }
        return file;
    }

    // === Header Operations ===========================================================================================

    /**
     * Writes a cell with the header style from {@link ExcelGenerator#createHeaderStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert ({@link String})
     */
    protected void writeHeaderCell(Row row, int cellIndex, String value) {
        Cell cell = row.createCell(cellIndex);
        cell.setCellValue(value);
        cell.setCellStyle(headerStyle);
    }

    /**
     * Writes a cell with the text style from {@link ExcelGenerator#createRowStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert as percent ({@link Integer} - ignores null)
     */
    protected void writeRowCellAsPercent(Row row, int cellIndex, Integer value) {
        if (value != null && value > 0) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value);
            cell.setCellStyle(createPercentageStyle(value));
        }
    }

    /**
     * Writes a cell with the date style from {@link ExcelGenerator#createDateStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert ({@link LocalDate} - ignores null)
     */
    protected void writeRowCell(Row row, int cellIndex, LocalDate value) {
        if (value != null) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value);
            cell.setCellStyle(dateStyle);
        }
    }

    /**
     * Writes a cell with the text style from {@link ExcelGenerator#createRowStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert ({@link String} - ignores null)
     */
    protected void writeRowCell(Row row, int cellIndex, String value) {
        if (value != null) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value);
            cell.setCellStyle(textStyle);
        }
    }

    /**
     * Writes a cell with the text style from {@link ExcelGenerator#createRowStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert ({@link Integer} - ignores null)
     */
    protected void writeRowCell(Row row, int cellIndex, Integer value) {
        if (value != null) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value);
            cell.setCellStyle(textStyle);
        }
    }

    /**
     * Writes a cell with the text style from {@link ExcelGenerator#createRowStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert ({@link Boolean} - ignores null)
     */
    protected void writeRowCell(Row row, int cellIndex, Boolean value) {
        if (value != null) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value);
            cell.setCellStyle(textStyle);
        }
    }

    /**
     * Writes a cell with the text style from {@link ExcelGenerator#createRowStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert (any {@link Enum} - ignores null)
     */
    protected void writeRowCell(Row row, int cellIndex, Enum<?> value) {
        if (value != null) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value.toString());
            cell.setCellStyle(textStyle);
        }
    }

    /**
     * Writes a cell with the text style from {@link ExcelGenerator#createRowStyle()}.
     *
     * @param row       of the sheet
     * @param cellIndex of the row
     * @param value     to insert ({@link List} - ignores null)
     */
    protected void writeRowCell(Row row, int cellIndex, List<Integer> value) {
        if (value != null && !value.isEmpty()) {
            Cell cell = row.createCell(cellIndex);
            cell.setCellValue(value.toString());
            cell.setCellStyle(textStyle);
        }
    }

    // === Styles ======================================================================================================

    /**
     * <ul>
     *     <li>Font: Arial</li>
     *     <li>Size: 11 + Bold</li>
     *     <li>Background: Solid Grey</li>
     * </ul>
     *
     * @return {@link CellStyle} for the header of the workbook
     */
    private CellStyle createHeaderStyle() {
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return headerStyle;
    }

    private CellStyle createPercentageStyle(int percentage) {
        CellStyle style = workbook.createCellStyle();
        if (percentage > 100) {
            style.setFillForegroundColor(IndexedColors.PINK.getIndex());
        } else if (percentage >= 95) {
            style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        } else if (percentage >= 75) {
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        } else if (percentage >= 50) {
            style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        } else if (percentage > 0) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
        }
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    /**
     * Default excel style.
     *
     * @return {@link CellStyle} for the content of the workbook
     */
    private CellStyle createRowStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    /**
     * Default excel style as date format from.
     *
     * @return {@link CellStyle} for the content (Date) of the workbook
     */
    private CellStyle createDateStyle() {
        CellStyle style = createRowStyle();
        style.setDataFormat(
                workbook.getCreationHelper().createDataFormat().getFormat("dd.MM.yyyy"));
        return style;
    }
}
