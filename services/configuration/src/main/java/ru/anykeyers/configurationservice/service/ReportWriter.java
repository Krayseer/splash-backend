package ru.anykeyers.configurationservice.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.remote.RemoteStorageService;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.web.mapper.ConfigurationMapper;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * Писатель PDF отчетов
 */
@Component
@RequiredArgsConstructor
public class ReportWriter {

    private final ConfigurationMapper configurationMapper;

    private final RemoteStorageService remoteStorageService;

    @SneakyThrows
    public ByteArrayOutputStream generateReport(Configuration configuration) {
        ConfigurationDTO report = configurationMapper.toDTO(configuration);

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Font font = getFont();

        PdfWriter.getInstance(document, outputStream);
        document.open();

        Paragraph title = new Paragraph("ОТЧЕТ ОРГАНИЗАЦИИ", font);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(15);

        PdfPTable orgInfoTable = new PdfPTable(2);
        orgInfoTable.setWidthPercentage(100);
        orgInfoTable.setSpacingAfter(15);

        PdfPCell orgInfoCell = new PdfPCell();
        orgInfoCell.setBorder(Rectangle.NO_BORDER);
        orgInfoCell.addElement(new Paragraph("Название: " + report.getOrganizationInfo().getName(), font));
        orgInfoCell.addElement(new Paragraph("ИНН: " + report.getOrganizationInfo().getTin(), font));
        orgInfoCell.addElement(new Paragraph("Тип: " + report.getOrganizationInfo().getType(), font));
        orgInfoCell.addElement(new Paragraph("Email: " + report.getOrganizationInfo().getEmail(), font));
        orgInfoCell.addElement(new Paragraph("Телефон: " + report.getOrganizationInfo().getPhoneNumber(), font));

        PdfPCell descriptionCell = new PdfPCell();
        descriptionCell.setBorder(Rectangle.NO_BORDER);
        descriptionCell.addElement(new Paragraph("Описание: " + report.getOrganizationInfo().getDescription(), font));

        orgInfoTable.addCell(orgInfoCell);
        orgInfoTable.addCell(descriptionCell);

        PdfPTable photosTable = new PdfPTable(2);
        photosTable.setWidthPercentage(100);
        photosTable.setSpacingAfter(15);

        for (String photoUrl : report.getPhotoUrls()) {
            try {
                ResponseEntity<Resource> response = remoteStorageService.getPhoto(photoUrl);
                if (response.getBody() != null) {
                    Resource resource = response.getBody();
                    byte[] photoBytes = resource.getInputStream().readAllBytes();
                    Image photo = Image.getInstance(photoBytes);
                    photo.scaleToFit(150, 150);

                    PdfPCell photoCell = new PdfPCell(photo);
                    photoCell.setBorder(Rectangle.NO_BORDER);
                    photosTable.addCell(photoCell);
                } else {
                    PdfPCell errorCell = new PdfPCell(new Phrase("Фотография недоступна"));
                    errorCell.setBorder(Rectangle.NO_BORDER);
                    photosTable.addCell(errorCell);
                }
            } catch (Exception e) {
                PdfPCell errorCell = new PdfPCell(new Phrase("Ошибка загрузки фотографии: " + e.getMessage()));
                errorCell.setBorder(Rectangle.NO_BORDER);
                photosTable.addCell(errorCell);
            }
        }

        PdfPTable servicesTable = new PdfPTable(3);
        servicesTable.setWidthPercentage(100);
        servicesTable.setSpacingAfter(15);
        servicesTable.addCell(new PdfPCell(new Phrase("Название услуги", font)));
        servicesTable.addCell(new PdfPCell(new Phrase("Длительность (мин)", font)));
        servicesTable.addCell(new PdfPCell(new Phrase("Цена (руб)", font)));

        for (ServiceDTO service : report.getServices()) {
            servicesTable.addCell(new PdfPCell(new Phrase(service.getName(), font)));
            servicesTable.addCell(new PdfPCell(new Phrase(String.valueOf(service.getDuration() / 60000), font)));
            servicesTable.addCell(new PdfPCell(new Phrase(String.valueOf(service.getPrice()), font)));
        }

        PdfPTable boxesTable = new PdfPTable(1);
        boxesTable.setWidthPercentage(100);
        boxesTable.setSpacingAfter(15);
        boxesTable.addCell(new PdfPCell(new Phrase("Боксы", font)));

        for (BoxDTO box : report.getBoxes()) {
            boxesTable.addCell(new PdfPCell(new Phrase(box.getName(), font)));
        }

        Paragraph workingHours = new Paragraph(
                "Часы работы: " + report.getOpenTime() + " - " + report.getCloseTime(), font
        );
        workingHours.setSpacingAfter(15);

        addElementsInDocument(document, title, orgInfoTable, photosTable, servicesTable, boxesTable, workingHours);
        document.close();
        return outputStream;
    }

    @SneakyThrows
    private void addElementsInDocument(Document document, Element... elements) {
        for (Element element : elements) {
            document.add(element);
        }
        document.close();
    }

    @SneakyThrows
    private Font getFont() {
        String fontPath = "/font/font-rus.ttf";
        byte[] fontBytes = IOUtils.toByteArray(Objects.requireNonNull(getClass().getResourceAsStream(fontPath)));
        BaseFont baseFont = BaseFont.createFont(
                "font-rus.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontBytes, null
        );
        return new Font(baseFont, 16, Font.NORMAL, BaseColor.BLACK);
    }

}
