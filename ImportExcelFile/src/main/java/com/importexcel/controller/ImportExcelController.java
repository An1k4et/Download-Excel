package com.importexcel.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.importexcel.repository.ImportExcelRepo;
import com.importexcel.service.ImportExcelService;

@Controller
public class ImportExcelController {
	
	@Autowired
	ImportExcelRepo importExcelRepo;
	
	@Autowired
	ImportExcelService importExcelService;
	
	@GetMapping
	public String homePage() {
		return "home";
	}
	
	@GetMapping("/data")
    public ResponseEntity<byte[]> exportDataToExcel() throws IOException {
        List<Map<String, Object>> employees = importExcelRepo.getEmployeeData();
        List<Map<String, Object>> clients = importExcelRepo.getClientData();
        
        for (Map<String, Object> employee : employees) {
            for (Map.Entry<String, Object> entry : employee.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println("---------------");
        }
        
        byte[] excelBytes = importExcelService.generateExcel(employees, clients);

		
		 return ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION,
		 "attachment; filename=employee_client_data.xlsx")
		 .contentType(MediaType.APPLICATION_OCTET_STREAM) .body(excelBytes);
		 
    }

}
