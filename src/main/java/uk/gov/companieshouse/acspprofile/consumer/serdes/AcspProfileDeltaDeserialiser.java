package uk.gov.companieshouse.acspprofile.consumer.serdes;


import static uk.gov.companieshouse.acspprofile.consumer.Application.NAMESPACE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.consumer.exception.NonRetryableException;
import uk.gov.companieshouse.acspprofile.consumer.logging.DataMapHolder;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AcspProfileDeltaDeserialiser {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private final ObjectMapper objectMapper;

    public AcspProfileDeltaDeserialiser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AcspProfileDelta deserialiseAcspProfileDelta(String data) {
        try {
            return objectMapper.readValue(data, AcspProfileDelta.class);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Unable to deserialise delta: [%s]".formatted(data), ex, DataMapHolder.getLogMap());
            throw new NonRetryableException("Unable to deserialise delta", ex);
        }
    }
}
