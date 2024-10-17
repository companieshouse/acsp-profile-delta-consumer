package uk.gov.companieshouse.acspprofile.consumer.service;

import static uk.gov.companieshouse.acspprofile.consumer.Application.NAMESPACE;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.consumer.apiclient.ApiClient;
import uk.gov.companieshouse.acspprofile.consumer.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.consumer.mapper.InternalAcspApiMapper;
import uk.gov.companieshouse.acspprofile.consumer.serdes.AcspProfileDeltaDeserialiser;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;
import uk.gov.companieshouse.delta.ChsDelta;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class UpsertDeltaService implements DeltaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    private final AcspProfileDeltaDeserialiser deltaDeserialiser;
    private final InternalAcspApiMapper mapper;
    private final ApiClient acspApiClient;

    public UpsertDeltaService(AcspProfileDeltaDeserialiser deltaDeserialiser, InternalAcspApiMapper mapper,
            ApiClient acspApiClient) {
        this.deltaDeserialiser = deltaDeserialiser;
        this.mapper = mapper;
        this.acspApiClient = acspApiClient;
    }

    @Override
    public void process(ChsDelta delta) {
        AcspProfileDelta acspProfileDelta = deltaDeserialiser.deserialiseAcspProfileDelta(delta.getData());

        String acspNumber = acspProfileDelta.getAcspNumber();
        DataMapHolder.get().acspNumber(acspNumber);

        InternalAcspApi requestBody = mapper.map(acspProfileDelta, delta.getContextId());
        acspApiClient.putAcspProfile(acspNumber, requestBody);

        LOGGER.info("Successfully called PUT ACSP profile", DataMapHolder.getLogMap());
    }
}
