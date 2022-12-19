package com.hft.provider.excel;

import com.hft.provider.controller.model.Feedback;
import com.hft.provider.controller.model.SolutionStatistic;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StatisticVisualizer extends ExcelGenerator {
    /**
     * Holds the Excel workbook to work on.
     */
    private StatisticVisualizer() {
        super();
    }

    public static File convert(List<SolutionStatistic> stats, List<Feedback> records30, List<Feedback> records60, List<Feedback> records90, List<Feedback> records120) throws IOException {
        StatisticVisualizer generator = new StatisticVisualizer();
        Sheet sheet30 = generator.createSheet("Size 30");
        generator.writeStatSheet(sheet30, stats.stream().filter(item -> item.getSize() == 30).toList(), records30);
        generator.configureSheet(sheet30);
        Sheet sheet60 = generator.createSheet("Size 60");
        generator.writeStatSheet(sheet60, stats.stream().filter(item -> item.getSize() == 60).toList(), records60);
        generator.configureSheet(sheet60);
        Sheet sheet90 = generator.createSheet("Size 90");
        generator.writeStatSheet(sheet90, stats.stream().filter(item -> item.getSize() == 90).toList(), records90);
        generator.configureSheet(sheet90);
        Sheet sheet120 = generator.createSheet("Size 120");
        generator.writeStatSheet(sheet120, stats.stream().filter(item -> item.getSize() == 120).toList(), records120);
        generator.configureSheet(sheet120);
        return generator.exportWorkbook();
    }

    private void writeStatSheet(Sheet sheet, List<SolutionStatistic> stats, List<Feedback> records) {
        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 0, "Size");
        writeHeaderCell(row, 1, "Par");
        writeHeaderCell(row, 2, "Inst");
        writeHeaderCell(row, 3, "Solution ID");
        writeHeaderCell(row, 4, "Creator");
        writeHeaderCell(row, 5, "Solution Makespan");
        writeHeaderCell(row, 6, "Record Timespan");
        writeHeaderCell(row, 7, "Difference");
        for (Feedback record : records) {
            row = sheet.createRow(rowIndex++);
            writeRowCell(row, 0, record.getSize());
            writeRowCell(row, 1, record.getPar());
            writeRowCell(row, 2, record.getInst());
            List<SolutionStatistic> statList = stats.stream().filter(
                    item -> item.getSize() == record.getSize()
                            && item.getPar() == record.getPar()
                            && item.getInst() == record.getInst()).toList();
            if (statList.isEmpty()) {
                writeRowCell(row, 3, "none");
            } else {
                writeRowCell(row, 5, statList.get(0).getMakespan());
                writeRowCell(row, 7, statList.get(0).getMakespan() - record.getRecordTimeSpan());
                String ids = "";
                String creators = "";
                for (SolutionStatistic itemStat : statList) {
                    ids += itemStat.getId() + "; ";
                    creators += itemStat.getCreator() + "; ";
                }
                writeRowCell(row, 3, ids);
                writeRowCell(row, 4, creators);

            }
            writeRowCell(row, 6, record.getRecordTimeSpan());
        }
    }

    private void configureSheet(Sheet sheet) {
        resizeSheet(sheet);
        sheet.setAutoFilter(new CellRangeAddress(
                0,
                sheet.getLastRowNum(),
                0,
                sheet.getRow(0).getLastCellNum() - 1));
        sheet.createFreezePane(0, 1);
    }

    private void resizeSheet(Sheet sheet) {
        sheet.setColumnWidth(0, 2500);
        sheet.setColumnWidth(1, 2500);
        sheet.setColumnWidth(2, 2500);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 6000);
        sheet.setColumnWidth(7, 4000);
    }
}
