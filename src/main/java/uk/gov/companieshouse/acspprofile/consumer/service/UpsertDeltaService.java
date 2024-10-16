package uk.gov.companieshouse.acspprofile.consumer.service;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.consumer.mapper.InternalAcspApiMapper;
import uk.gov.companieshouse.acspprofile.consumer.serdes.AcspProfileDeltaDeserialiser;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;
import uk.gov.companieshouse.delta.ChsDelta;

@Component
public class UpsertDeltaService implements DeltaService {

    private final AcspProfileDeltaDeserialiser deltaDeserialiser;
    private final InternalAcspApiMapper mapper;

    public UpsertDeltaService(AcspProfileDeltaDeserialiser deltaDeserialiser, InternalAcspApiMapper mapper) {
        this.deltaDeserialiser = deltaDeserialiser;
        this.mapper = mapper;
    }

    @Override
    public void process(ChsDelta delta) {
        AcspProfileDelta acspProfileDelta = deltaDeserialiser.deserialiseAcspProfileDelta(delta.getData());
        mapper.map(acspProfileDelta, delta.getContextId());
    }
}
