package com.example.tbm_task.service;

import com.example.tbm_task.model.Result;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.*;
import java.util.List;


public class Main {



    public static void main(String[] args) {
        Main docGenerator = new Main();
    }

    public ByteArrayInputStream getExcelFile(List<Result> results) {

        File file = null;

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet(" Result ");
            XSSFRow row;
            XSSFRow row2;

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);


            int rowed = 0;
            row = spreadsheet.createRow(rowed++);

            row.createCell(0).setCellValue("Bts Name (ID)");
            row.getCell(0).setCellStyle(cellStyle);

            row.createCell(1).setCellValue("Logic Id");
            row.getCell(1).setCellStyle(cellStyle);

            row.createCell(2).setCellValue("Time Up");
            row.getCell(2).setCellStyle(cellStyle);

            row.createCell(3).setCellValue("Time Down");
            row.getCell(3).setCellStyle(cellStyle);

            row.createCell(4).setCellValue("Duration");
            row.getCell(4).setCellStyle(cellStyle);

            row.createCell(5).setCellValue("Topology");
            row.getCell(5).setCellStyle(cellStyle);

            row.createCell(6).setCellValue("Additional Info");
            row.getCell(6).setCellStyle(cellStyle);

            row.createCell(7).setCellValue("Latitude");
            row.getCell(7).setCellStyle(cellStyle);

            row.createCell(8).setCellValue("Longitude");
            row.getCell(8).setCellStyle(cellStyle);


            int i = 1;

            for (Result result : results) {
                row = spreadsheet.createRow(rowed++);
                row.createCell(0).setCellValue(""+result.getDevice_name_id());
                row.getCell(0).setCellStyle(cellStyle);
                row.createCell(1).setCellValue(""+result.getLogic_id());
                row.getCell(1).setCellStyle(cellStyle);

                row.createCell(2).setCellValue(""+result.getTime_up());
                row.getCell(2).setCellStyle(cellStyle);

                row.createCell(3).setCellValue(""+result.getTime_down());
                row.getCell(3).setCellStyle(cellStyle);

                row.createCell(4).setCellValue(""+result.getDuration());
                row.getCell(4).setCellStyle(cellStyle);

                row.createCell(5).setCellValue(""+result.getTopology());
                row.getCell(5).setCellStyle(cellStyle);

                row.createCell(6).setCellValue(""+result.getAdditional_info());
                row.getCell(6).setCellStyle(cellStyle);

                row.createCell(7).setCellValue(""+result.getLatitude());
                row.getCell(7).setCellStyle(cellStyle);

                row.createCell(8).setCellValue(""+result.getLongitude());
                row.getCell(8).setCellStyle(cellStyle);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
