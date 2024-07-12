package uk.gov.companieshouse.acspprofile.consumer.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;

final class KafkaUtils {

    static final String MAIN_TOPIC = "acsp-profile-delta";
    static final String RETRY_TOPIC = "acsp-profile-delta-acsp-profile-delta-consumer-retry";
    static final String ERROR_TOPIC = "acsp-profile-delta-acsp-profile-delta-consumer-error";
    static final String INVALID_TOPIC = "acsp-profile-delta-acsp-profile-delta-consumer-invalid";

    private KafkaUtils() {
    }

    static int noOfRecordsForTopic(ConsumerRecords<?, ?> records, String topic) {
        int count = 0;
        for (ConsumerRecord<?, ?> ignored : records.records(topic)) {
            count++;
        }
        return count;
    }

    static Duration kafkaPollingDuration() {
        String kafkaPollingDuration = System.getenv().containsKey("KAFKA_POLLING_DURATION") ?
                System.getenv("KAFKA_POLLING_DURATION") : "1000";
        return Duration.ofMillis(Long.parseLong(kafkaPollingDuration));
    }
}
