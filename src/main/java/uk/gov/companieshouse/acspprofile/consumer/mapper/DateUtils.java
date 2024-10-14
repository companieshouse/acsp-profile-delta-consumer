package uk.gov.companieshouse.acspprofile.consumer.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

final class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private DateUtils() {
    }

    static LocalDate stringToLocalDate(final String inputDate) {
        return Optional.ofNullable(inputDate)
                .map(date -> LocalDate.parse(date, FORMATTER))
                .orElse(null);
    }
}