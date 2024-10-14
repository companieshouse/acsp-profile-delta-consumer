package uk.gov.companieshouse.acspprofile.consumer.mapper;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.GenerateEtagUtil;

@Component
class EtagGenerator {

    String generateEtag() {
        return GenerateEtagUtil.generateEtag();
    }
}
