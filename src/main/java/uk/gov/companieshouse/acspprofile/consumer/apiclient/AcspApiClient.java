package uk.gov.companieshouse.acspprofile.consumer.apiclient;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.consumer.logging.DataMapHolder;
import uk.gov.companieshouse.api.InternalApiClient;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;

@Component
public class AcspApiClient implements ApiClient {

    private static final String REQUEST_URI = "/authorised-corporate-service-providers/%s/internal";

    private final InternalApiClientFactory internalApiClientFactory;
    private final ResponseHandler responseHandler;

    public AcspApiClient(InternalApiClientFactory internalApiClientFactory, ResponseHandler responseHandler) {
        this.internalApiClientFactory = internalApiClientFactory;
        this.responseHandler = responseHandler;
    }

    @Override
    public void putAcspProfile(String acspNumber, InternalAcspApi requestBody) {
        InternalApiClient client = internalApiClientFactory.get();
        client.getHttpClient().setRequestId(DataMapHolder.getRequestId());

        final String formattedUri = REQUEST_URI.formatted(acspNumber);
        try {
            client.privateDeltaResourceHandler()
                    .putAcspProfile(formattedUri, requestBody)
                    .execute();
        } catch (ApiErrorResponseException ex) {
            responseHandler.handle(ex);
        } catch (URIValidationException ex) {
            responseHandler.handle(ex);
        }
    }
}
