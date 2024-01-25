package com.nobroker.controller;

import com.nobroker.Repository.UserRepository;
import com.nobroker.entity.User;
import com.nobroker.service.impl.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/exportUsers")
    public ResponseEntity<Object> exportUsersToExcel() {
        try {
            List<User> users = userRepository.findAll();// Retrieve users from your database or service;

            byte[] excelData = excelService.exportUsersToExcel(users);

            if (excelData != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "users.xlsx");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(excelData);
            } else {
                // Handle export failure
                return ResponseEntity.status(500).body("Failed to export users to Excel.");
            }
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(500).body("An error occurred while exporting users to Excel.");
        }
    }
}

