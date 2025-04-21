package com.inventory.services;

import com.inventory.entities.Invoice;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    public byte[] generateInvoicePdf(Invoice invoice) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
     // Set up PDF writer and document
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add invoice details
        document.add(new Paragraph("Invoice #: " + invoice.getInvoiceNumber()));
        document.add(new Paragraph("Total: ₹" + invoice.getTotalAmount()));
        document.add(new Paragraph("Tax: ₹" + invoice.getTaxAmount()));
        document.add(new Paragraph("Discount: ₹" + invoice.getDiscountAmount()));
        document.add(new Paragraph("Final Amount: ₹" + invoice.getFinalAmount()));
        document.add(new Paragraph("Status: " + invoice.getPaymentStatus()));

        document.close();
        return out.toByteArray();
    }
}
