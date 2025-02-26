package com.importexcel.service;

import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImportExcelService {
	public byte[] generateExcel(List<Map<String, Object>> employees, List<Map<String, Object>> clients) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Create Employee Sheet
        createSheet(workbook, "employees_data", employees);

        // Create Client Sheet
        createSheet(workbook, "clients_data", clients);

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    private void createSheet(Workbook workbook, String sheetName, List<Map<String, Object>> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        if (data.isEmpty()) return;

        // Create header row
        Row headerRow = sheet.createRow(0);
        Map<String, Object> firstRow = data.get(0);
        String[] headers = firstRow.keySet().toArray(new String[0]);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Create data rows
        int rowNum = 1;
        for (Map<String, Object> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String key : headers) {
                Cell cell = row.createCell(colNum++);
                Object value = rowData.get(key);
                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                } else {
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }
        }
    }
}
