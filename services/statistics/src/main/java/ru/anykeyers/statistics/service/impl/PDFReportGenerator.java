package ru.anykeyers.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.anykeyers.statistics.domain.Statistics;
import ru.anykeyers.statistics.service.ReportGenerator;

/**
 * PDF генератор отчетов
 */
@Component
@RequiredArgsConstructor
public class PDFReportGenerator implements ReportGenerator {

    @Override
    public byte[] generate(Statistics statistics) {
        return new byte[0];
    }



}
