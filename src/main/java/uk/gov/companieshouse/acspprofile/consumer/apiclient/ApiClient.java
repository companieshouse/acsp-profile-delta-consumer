package uk.gov.companieshouse.acspprofile.consumer.apiclient;

import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

public interface ApiClient {

    void putAcspProfile(String acspNumber, InternalAcspApi requestBody);
}
