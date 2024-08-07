package uk.gov.companieshouse.acspprofile.consumer.apiclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.consumer.exception.NonRetryableException;
import uk.gov.companieshouse.acspprofile.consumer.exception.RetryableException;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseHandlerTest {

    private final ResponseHandler responseHandler = new ResponseHandler();

    @Mock
    private ApiErrorResponseException apiErrorResponseException;
    @Mock
    private URIValidationException uriValidationException;
    @Mock
    private IllegalArgumentException illegalArgumentException;
    @Mock
    private Throwable throwable;

    @ParameterizedTest
    @CsvSource({
            "500",
            "501",
            "502",
            "503",
            "504"
    })
    void shouldHandleApiErrorResponseByThrowingRetryableExceptionWhen5xxStatusCode(final int httpStatusCode) {
        // given
        when(apiErrorResponseException.getStatusCode()).thenReturn(httpStatusCode);

        // when
        Executable executable = () -> responseHandler.handle(apiErrorResponseException);

        // then
        assertThrows(RetryableException.class, executable);
    }

    @ParameterizedTest
    @CsvSource({
            "400",
            "401",
            "403",
            "404",
            "405",
            "409"
    })
    void shouldHandleApiErrorResponseByThrowingNonRetryableExceptionWhenNot5xxStatusCode(final int httpStatusCode) {
        // given
        when(apiErrorResponseException.getStatusCode()).thenReturn(httpStatusCode);

        // when
        Executable executable = () -> responseHandler.handle(apiErrorResponseException);

        // then
        assertThrows(NonRetryableException.class, executable);
    }

    @Test
    void shouldHandleUriValidationExceptionByThrowingNonRetryableException() {
        // given

        // when
        Executable executable = () -> responseHandler.handle(uriValidationException);

        // then
        assertThrows(NonRetryableException.class, executable);
    }
}
