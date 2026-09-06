package com.srms.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.srms.model.Attendance;
import com.srms.model.FeeRecord;
import com.srms.model.Mark;
import com.srms.model.Student;

import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.util.List;

/**
 * ReportPdfService - PDF Report Generator.
 * Generates formatted PDF reports for Marksheet, Attendance Summary, and Fee Receipt.
 */
public class ReportPdfService {

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.decode("#1E3A8A"));
    private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.decode("#2563EB"));
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
    private static final Font TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.decode("#0F172A"));
    private static final Font BOLD_TEXT_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.decode("#0F172A"));

    public byte[] generateMarksheetPdf(Student student, List<Mark> marksList) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        // Header
        Paragraph title = new Paragraph("STUDENT RECORD MANAGEMENT SYSTEM (SRMS)", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("ACADEMIC MARKSHEET / REPORT CARD", SUBTITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(15);
        document.add(subtitle);

        // Student Metadata Table
        PdfPTable metaTable = new PdfPTable(2);
        metaTable.setWidthPercentage(100);
        metaTable.setSpacingAfter(15);

        addMetaCell(metaTable, "Student Name:", student.getName());
        addMetaCell(metaTable, "Roll Number:", student.getRollNo());
        addMetaCell(metaTable, "Department:", student.getDeptName());
        addMetaCell(metaTable, "Semester:", "Semester " + student.getSemester());

        document.add(metaTable);

        // Marks Table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 4, 2, 2, 2});

        addHeaderCell(table, "Code");
        addHeaderCell(table, "Subject Name");
        addHeaderCell(table, "Exam Type");
        addHeaderCell(table, "Marks Obtained");
        addHeaderCell(table, "Max Marks");

        double totalObtained = 0;
        double totalMax = 0;

        for (Mark m : marksList) {
            addBodyCell(table, m.getSubjectCode());
            addBodyCell(table, m.getSubjectName());
            addBodyCell(table, m.getExamType());
            addBodyCell(table, String.format("%.2f", m.getMarksObtained()));
            addBodyCell(table, String.format("%.2f", m.getMaxMarks()));

            totalObtained += m.getMarksObtained();
            totalMax += m.getMaxMarks();
        }

        document.add(table);

        // Summary Total
        Paragraph summary = new Paragraph(
            String.format("Total Marks: %.2f / %.2f (Percentage: %.2f%%)",
                totalObtained, totalMax, totalMax > 0 ? (totalObtained / totalMax) * 100 : 0.0),
            BOLD_TEXT_FONT
        );
        summary.setSpacingBefore(15);
        summary.setAlignment(Element.ALIGN_RIGHT);
        document.add(summary);

        document.close();
        return out.toByteArray();
    }

    public byte[] generateAttendanceReportPdf(Student student, List<Attendance> attendanceList, double overallPercentage) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        Paragraph title = new Paragraph("STUDENT RECORD MANAGEMENT SYSTEM (SRMS)", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("ATTENDANCE SUMMARY REPORT", SUBTITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(15);
        document.add(subtitle);

        // Student Info
        PdfPTable metaTable = new PdfPTable(2);
        metaTable.setWidthPercentage(100);
        metaTable.setSpacingAfter(15);

        addMetaCell(metaTable, "Student Name:", student.getName());
        addMetaCell(metaTable, "Roll Number:", student.getRollNo());
        addMetaCell(metaTable, "Overall Attendance:", String.format("%.2f%%", overallPercentage));
        addMetaCell(metaTable, "Status:", overallPercentage >= 75.0 ? "SATISFACTORY (Eligible)" : "DEFAULTER (< 75%)");

        document.add(metaTable);

        // Attendance Table
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 4, 2, 2, 2});

        addHeaderCell(table, "Date");
        addHeaderCell(table, "Subject");
        addHeaderCell(table, "Subject Code");
        addHeaderCell(table, "Period");
        addHeaderCell(table, "Status");

        for (Attendance a : attendanceList) {
            addBodyCell(table, a.getAttendanceDate().toString());
            addBodyCell(table, a.getSubjectName());
            addBodyCell(table, a.getSubjectCode());
            addBodyCell(table, "Period " + a.getPeriodNo());
            addBodyCell(table, a.getStatus());
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    public byte[] generateFeeReceiptPdf(Student student, FeeRecord fee) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, out);

        document.open();

        Paragraph title = new Paragraph("STUDENT RECORD MANAGEMENT SYSTEM (SRMS)", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("OFFICIAL FEE PAYMENT RECEIPT", SUBTITLE_FONT);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        PdfPTable receiptTable = new PdfPTable(2);
        receiptTable.setWidthPercentage(100);
        receiptTable.setSpacingAfter(20);

        addMetaCell(receiptTable, "Receipt No:", fee.getReceiptNo() != null ? fee.getReceiptNo() : "REC-PENDING");
        addMetaCell(receiptTable, "Payment Date:", fee.getPaymentDate() != null ? fee.getPaymentDate().toString() : "N/A");
        addMetaCell(receiptTable, "Student Name:", student.getName());
        addMetaCell(receiptTable, "Roll Number:", student.getRollNo());
        addMetaCell(receiptTable, "Semester:", "Semester " + fee.getSemester());
        addMetaCell(receiptTable, "Department:", student.getDeptName());
        addMetaCell(receiptTable, "Fee Amount Paid:", String.format("INR %.2f", fee.getAmount()));
        addMetaCell(receiptTable, "Payment Status:", fee.getStatus());

        document.add(receiptTable);

        Paragraph footer = new Paragraph("This is a computer-generated receipt issued by SRMS College Portal.", BOLD_TEXT_FONT);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return out.toByteArray();
    }

    private void addMetaCell(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, BOLD_TEXT_FONT));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(5);
        table.addCell(cellLabel);

        PdfPCell cellVal = new PdfPCell(new Phrase(value != null ? value : "-", TEXT_FONT));
        cellVal.setBorder(Rectangle.NO_BORDER);
        cellVal.setPadding(5);
        table.addCell(cellVal);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(Color.decode("#1E3A8A"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "-", TEXT_FONT));
        cell.setPadding(6);
        table.addCell(cell);
    }
}
