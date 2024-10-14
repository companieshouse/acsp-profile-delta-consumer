package uk.gov.companieshouse.acspprofile.consumer.kafka;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.delta.ChsDelta;

@Component
public class DeltaServiceRouter {

    void route(ChsDelta payload) {
        // Route the delta to the appropriate consumer based on the type
    }
}
