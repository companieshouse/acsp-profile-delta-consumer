package uk.gov.companieshouse.acspprofile.consumer.kafka;

import com.google.common.collect.Iterables;
import org.apache.kafka.clients.consumer.ConsumerRecords;

final class KafkaUtils {

    static final String MAIN_TOPIC = "acsp-profile-delta";
    static final String RETRY_TOPIC = "acsp-profile-delta-acsp-profile-delta-consumer-retry";
    static final String ERROR_TOPIC = "acsp-profile-delta-acsp-profile-delta-consumer-error";
    static final String INVALID_TOPIC = "acsp-profile-delta-acsp-profile-delta-consumer-invalid";

    private KafkaUtils() {
    }

    static int noOfRecordsForTopic(ConsumerRecords<?, ?> records, String topic) {
        return Iterables.size(records.records(topic));
    }
}
