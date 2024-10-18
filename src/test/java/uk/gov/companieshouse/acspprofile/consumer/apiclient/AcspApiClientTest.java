package uk.gov.companieshouse.acspprofile.consumer.apiclient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.consumer.logging.DataMapHolder;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.delta.PrivateDeltaResourceHandler;
import uk.gov.companieshouse.api.handler.delta.acspprofile.request.PrivateAcspProfilePut;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.http.HttpClient;

@ExtendWith(MockitoExtension.class)
class AcspApiClientTest {

    private static final String ACSP_NUMBER = "AP123456";
    private static final String REQUEST_URI = "/authorised-corporate-service-providers/%s/internal"
            .formatted(ACSP_NUMBER);
    private static final String CONTEXT_ID = "context_id";

    @InjectMocks
    private AcspApiClient acspApiClient;

    @Mock
    private InternalApiClientFactory internalApiClientFactory;
    @Mock
    private ResponseHandler responseHandler;

    @Mock
    private InternalAcspApi requestBody;
    @Mock
    private InternalApiClient internalApiClient;
    @Mock
    private HttpClient apiClient;
    @Mock
    private PrivateDeltaResourceHandler privateDeltaResourceHandler;
    @Mock
    private PrivateAcspProfilePut privateAcspProfilePut;

    @BeforeEach
    void whenApiClientStubbing() {
        when(internalApiClientFactory.get()).thenReturn(internalApiClient);
        when(internalApiClient.getHttpClient()).thenReturn(apiClient);
        when(internalApiClient.privateDeltaResourceHandler()).thenReturn(privateDeltaResourceHandler);
        when(privateDeltaResourceHandler.putAcspProfile(any(), any())).thenReturn(privateAcspProfilePut);
    }

    @AfterEach
    void verifyApiClientCalls() throws ApiErrorResponseException, URIValidationException {
        verify(apiClient).setRequestId(CONTEXT_ID);
        verify(internalApiClient).privateDeltaResourceHandler();
        verify(privateDeltaResourceHandler).putAcspProfile(REQUEST_URI, requestBody);
        verify(privateAcspProfilePut).execute();
    }

    @Test
    void shouldSendSuccessfulPutRequest() {
        // given
        DataMapHolder.get().requestId(CONTEXT_ID);

        // when
        acspApiClient.putAcspProfile(ACSP_NUMBER, requestBody);

        // then
        verifyNoInteractions(responseHandler);
    }

    @Test
    void shouldHandleApiErrorExceptionWhenSendingPutRequest() throws Exception {
        // given
        Class<ApiErrorResponseException> exceptionClass = ApiErrorResponseException.class;

        when(privateAcspProfilePut.execute()).thenThrow(exceptionClass);

        DataMapHolder.get().requestId(CONTEXT_ID);

        // when
        acspApiClient.putAcspProfile(ACSP_NUMBER, requestBody);

        // then
        verify(responseHandler).handle(any(exceptionClass));
    }

    @Test
    void shouldHandleURIValidationExceptionWhenSendingPutRequest() throws Exception {
        // given
        Class<URIValidationException> exceptionClass = URIValidationException.class;

        when(privateAcspProfilePut.execute()).thenThrow(exceptionClass);

        DataMapHolder.get().requestId(CONTEXT_ID);

        // when
        acspApiClient.putAcspProfile(ACSP_NUMBER, requestBody);

        // then
        verify(responseHandler).handle(any(exceptionClass));
    }
}