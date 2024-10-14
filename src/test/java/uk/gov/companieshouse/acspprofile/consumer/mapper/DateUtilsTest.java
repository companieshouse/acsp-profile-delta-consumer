package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2020, 8, 21);
    private static final String LOCAL_DATE_STRING = "20200821";


    @Test
    void shouldConvertDateStringToLocalDate() {
        // given

        // when
        LocalDate actual = DateUtils.stringToLocalDate(LOCAL_DATE_STRING);

        // then
        assertEquals(LOCAL_DATE, actual);
    }

    @Test
    void shouldReturnNullGivenNullString() {
        // given

        // when
        LocalDate actual = DateUtils.stringToLocalDate(null);

        // then
        assertNull(actual);
    }
}