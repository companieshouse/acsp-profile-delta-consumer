package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EtagGeneratorTest {

    private final EtagGenerator etagGenerator = new EtagGenerator();

    @Test
    void shouldGenerateEtagString() {
        assertNotNull(etagGenerator.generateEtag());
    }
}