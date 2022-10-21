package com.hft.provider.excel;

import com.hft.provider.controller.model.Job;
import com.hft.provider.controller.model.Project;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;

public class ProjectVisualizer extends ExcelGenerator {

    /**
     * Holds the Excel workbook to work on.
     */
    private ProjectVisualizer() {
        super();
    }

    public static File convert(Project solution) throws IOException {
        ProjectVisualizer generator = new ProjectVisualizer();
        Sheet visualSheet = generator.createSheet("Visualization");
        generator.writeResourceVisualization(visualSheet, solution);
        generator.resizeVisualizationSheet(visualSheet);
        Sheet dataSheet = generator.createSheet("Data");
        generator.writeProjectData(dataSheet, solution);
        generator.resizeDataSheet(dataSheet);
        return generator.exportWorkbook();
    }

    private void writeResourceVisualization(Sheet sheet, Project solution) {
        int[][] usagePerDay = collectResourceUsage(solution);
        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 1, "R1 (% of " + solution.getR1CapacityPerDay() + "h)");
        writeHeaderCell(row, 2, "R2 (% of " + solution.getR2CapacityPerDay() + "h)");
        writeHeaderCell(row, 3, "R3 (% of " + solution.getR3CapacityPerDay() + "h)");
        writeHeaderCell(row, 4, "R4 (% of " + solution.getR4CapacityPerDay() + "h)");
        for (int day = 0; day < usagePerDay[0].length; day++) {
            row = sheet.createRow(rowIndex++);
            writeHeaderCell(row, 0, "Day " + (day + 1));
            writeRowCellAsPercent(row, 1, usagePerDay[0][day]);
            writeRowCellAsPercent(row, 2, usagePerDay[1][day]);
            writeRowCellAsPercent(row, 3, usagePerDay[2][day]);
            writeRowCellAsPercent(row, 4, usagePerDay[3][day]);
        }
    }

    private void resizeVisualizationSheet(Sheet sheet) {
        sheet.setColumnWidth(0, 2500);
        sheet.setColumnWidth(1, 3300);
        sheet.setColumnWidth(2, 3300);
        sheet.setColumnWidth(3, 3300);
        sheet.setColumnWidth(4, 3300);
    }

    private int[][] collectResourceUsage(Project solution) {
        int[][] usagePerDay = new int[4][solution.getHorizon()];
        // collect absolut usage per day
        for (Job job : solution.getJobs()) {
            Integer start = job.getStartDay();
            int duration = job.getDurationDays();
            if (start != null) {
                for (int day = start; day < start + duration; day++) {
                    usagePerDay[0][day] += job.getR1HoursPerDay();
                    usagePerDay[1][day] += job.getR2HoursPerDay();
                    usagePerDay[2][day] += job.getR3HoursPerDay();
                    usagePerDay[3][day] += job.getR4HoursPerDay();
                }
            }
        }
        // transfer usage to percentage per day
        for (int day = 0; day < usagePerDay[0].length; day++) {
            usagePerDay[0][day] = usagePerDay[0][day] * 100 / solution.getR1CapacityPerDay();
            usagePerDay[1][day] = usagePerDay[1][day] * 100 / solution.getR2CapacityPerDay();
            usagePerDay[2][day] = usagePerDay[2][day] * 100 / solution.getR3CapacityPerDay();
            usagePerDay[3][day] = usagePerDay[3][day] * 100 / solution.getR4CapacityPerDay();
        }
        return usagePerDay;
    }

    private void writeProjectData(Sheet sheet, Project project) {
        int rowIndex = 0;
        Row row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 1, "Size");
        writeHeaderCell(row, 2, "Par");
        writeHeaderCell(row, 3, "Inst");
        writeHeaderCell(row, 9, "Jobs");
        writeHeaderCell(row, 10, "Horizon");
        row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 0, "Project");
        writeRowCell(row, 1, project.getSize());
        writeRowCell(row, 2, project.getPar());
        writeRowCell(row, 3, project.getInst());
        writeRowCell(row, 9, project.getJobCount());
        writeRowCell(row, 10, project.getHorizon());
        rowIndex += 2;
        row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 1, "R1");
        writeHeaderCell(row, 2, "R2");
        writeHeaderCell(row, 3, "R3");
        writeHeaderCell(row, 4, "R4");
        row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 0, "Capacity Per Day");
        writeRowCell(row, 1, project.getR1CapacityPerDay());
        writeRowCell(row, 2, project.getR2CapacityPerDay());
        writeRowCell(row, 3, project.getR3CapacityPerDay());
        writeRowCell(row, 4, project.getR4CapacityPerDay());
        rowIndex += 2;
        row = sheet.createRow(rowIndex++);
        writeHeaderCell(row, 1, "Mode");
        writeHeaderCell(row, 2, "Duration (days)");
        writeHeaderCell(row, 3, "Start Day");
        writeHeaderCell(row, 4, "End Day");
        writeHeaderCell(row, 5, "R1 Hours (per day)");
        writeHeaderCell(row, 6, "R2 Hours (per day)");
        writeHeaderCell(row, 7, "R3 Hours (per day)");
        writeHeaderCell(row, 8, "R4 Hours (per day)");
        writeHeaderCell(row, 9, "Predecessors");
        writeHeaderCell(row, 10, "Successors");
        for (Job job : project.getJobs()) {
            row = sheet.createRow(rowIndex++);
            writeHeaderCell(row, 0, "Job " + job.getNr());
            writeRowCell(row, 1, job.getMode());
            writeRowCell(row, 2, job.getDurationDays());
            writeRowCell(row, 3, job.getStartDay());
            Integer endDay = job.getStartDay() != null ? job.getStartDay() + job.getDurationDays() : null;
            writeRowCell(row, 4, endDay);
            writeRowCell(row, 5, job.getR1HoursPerDay());
            writeRowCell(row, 6, job.getR2HoursPerDay());
            writeRowCell(row, 7, job.getR3HoursPerDay());
            writeRowCell(row, 8, job.getR4HoursPerDay());
            writeRowCell(row, 9, job.getPredecessors());
            writeRowCell(row, 10, job.getSuccessors());
        }
    }

    private void resizeDataSheet(Sheet sheet) {
        sheet.setColumnWidth(0, 2300);
        sheet.setColumnWidth(1, 2250);
        sheet.setColumnWidth(2, 2200);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 2500);
        sheet.setColumnWidth(5, 880);
        sheet.setColumnWidth(6, 880);
        sheet.setColumnWidth(7, 880);
        sheet.setColumnWidth(8, 880);
        sheet.setColumnWidth(9, 3600);
        sheet.setColumnWidth(10, 3600);
    }
}
