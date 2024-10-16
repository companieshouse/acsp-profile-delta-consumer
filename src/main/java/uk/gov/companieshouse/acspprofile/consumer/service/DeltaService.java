package uk.gov.companieshouse.acspprofile.consumer.service;

import uk.gov.companieshouse.delta.ChsDelta;

public interface DeltaService {

    void process(ChsDelta delta);
}
