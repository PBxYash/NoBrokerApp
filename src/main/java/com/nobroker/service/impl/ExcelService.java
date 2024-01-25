package com.nobroker.service.impl;

import com.nobroker.entity.User;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public byte[] exportUsersToExcel(List<User> users) {
        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Users");

            // Create header row
            var headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Email");
            headerRow.createCell(3).setCellValue("Mobile");
            headerRow.createCell(4).setCellValue("Email Verified");

            // Create data rows
            int rowNum = 1;
            for (var user : users) {
                var row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getMobile());
                row.createCell(4).setCellValue(user.isEmailVerified());
            }

            // Write to ByteArrayOutputStream
            var outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception as needed
            return null;
        }
    }
}
