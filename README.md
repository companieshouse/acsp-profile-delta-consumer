# acsp-profile-delta-consumer
acsp-profile-delta-consumer is a Java service which utilizes Spring Kafka to process acsp-profile deltas. It consumes the deltas from the acsp-profile-deltas Kafka topic and transforms them before sending requests to acsp-data-api for the delta to be persisted.
