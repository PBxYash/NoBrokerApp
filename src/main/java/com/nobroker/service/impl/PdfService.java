package com.nobroker.service.impl;

import com.nobroker.Repository.UserRepository;
import com.nobroker.entity.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Resource> generatePdf() {
        List<User> users = userRepository.findAll();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font and other properties as needed
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float tableHeight = 20f;
                float rowHeight = 15f;
                float tableYBottom = yStart - tableHeight;

                // Draw table header
                drawTableHeader(contentStream, margin, yStart, tableWidth, tableHeight);

                // Draw table rows
                for (User user : users) {
                    drawTableRow(contentStream, margin, tableWidth, rowHeight, yPosition, user);
                    yPosition -= rowHeight;
                }

                // Draw table bottom border
                contentStream.drawLine(margin, tableYBottom, margin + tableWidth, tableYBottom);

                // Add signature line
                float signatureY = tableYBottom - 30;
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, signatureY);
                contentStream.showText("Signature: Yash Kumar");
                contentStream.endText();
            }

            // Save the PDF content to a ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            // Return the PDF as a ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user-details.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            // Return ResponseEntity with the PDF content and headers
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(baos.size())
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception and return an error response
            return ResponseEntity.status(500).body(new ByteArrayResource(new byte[0]));
        }
    }

    private void drawTableHeader(PDPageContentStream contentStream, float margin, float yStart, float tableWidth, float tableHeight) throws IOException {
        contentStream.setLineWidth(1f);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yStart);
        contentStream.showText("User ID");
        contentStream.newLineAtOffset(80, 0);
        contentStream.showText("Name");
        contentStream.newLineAtOffset(150, 0);
        contentStream.showText("Email");
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText("Mobile");
        contentStream.endText();
        contentStream.setLineWidth(1.5f);
        contentStream.moveTo(margin, yStart - tableHeight);
        contentStream.lineTo(margin + tableWidth, yStart - tableHeight);
        contentStream.stroke();
    }

    private void drawTableRow(PDPageContentStream contentStream, float margin, float tableWidth, float rowHeight, float yPosition, User user) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(margin + tableWidth, yPosition);
        contentStream.stroke();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 15);
        contentStream.showText(String.valueOf(user.getId()));
        contentStream.newLineAtOffset(75, 0);
        contentStream.showText(user.getName());
        contentStream.newLineAtOffset(145, 0);
        contentStream.showText(user.getEmail());
        contentStream.newLineAtOffset(115, 0);
        contentStream.showText(user.getMobile());
        contentStream.endText();
    }
}
