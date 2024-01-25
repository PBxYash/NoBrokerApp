package com.nobroker.controller;

import com.nobroker.service.impl.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfGenerationService;

    @GetMapping("/generate")
    public ResponseEntity<Resource> generatePdf() {
        return pdfGenerationService.generatePdf();
    }
}
