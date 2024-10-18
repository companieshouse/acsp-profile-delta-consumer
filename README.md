# acsp-profile-delta-consumer

``acsp-profile-delta-consumer`` handles the processing of ACSP profile deltas by:

* consuming them, in the forms of `ChsDelta` Kafka messages, from the `acsp-profile-delta` Kafka topic,
* deserialising and transforming them into a structure suitable for a request to `acsp-profile-data-api`, and
* sending the request internally while performing any error handling.

## System requirements

* [Git](https://git-scm.com/downloads)
* [Java](http://www.oracle.com/technetwork/java/javase/downloads)
* [Maven](https://maven.apache.org/download.cgi)
* [Apache Kafka](https://kafka.apache.org/)

## Getting started

### Building and running locally using docker

1. Clone [Docker CHS Development](https://github.com/companieshouse/docker-chs-development) and follow the steps in the
   README.
2. Enable the required services by running the following command in the `docker-chs-development` directory
   ```
   chs-dev services enable chs-delta-api \
   acsp-profile-delta-consumer \
   acsp-profile-data-api \
   authentication-service \ 
   ch-gov-uk
   ```
3. Boot up the services' containers on docker using `chs-dev up`.
4. Messages can be produced to the acsp-profile-delta topic using the instructions given
   in [CHS Delta API](https://github.com/companieshouse/chs-delta-api).

### Building the docker image with local changes, requires access to AWS ECR

```bash
  mvn package -Dskip.unit.tests=true -Dskip.integration.tests=true jib:dockerBuild
```

### Environment variables

| Variable                      | Description                                                                                   | Example (from docker-chs-development) |
|-------------------------------|-----------------------------------------------------------------------------------------------|---------------------------------------|
| ACSP_PROFILE_API_KEY          | The client ID of an API key, with internal app privileges, to call acsp-profile-data-api with | abc123def456ghi789                    |
| API_LOCAL_URL                 | The host through which requests to the acsp-profile-data-api are sent                         | http://api.chs.local:4001             |
| BOOTSTRAP_SERVER_URL          | The URL to the kafka broker                                                                   | kafka:9092                            |
| CONCURRENT_LISTENER_INSTANCES | The number of listeners run in parallel for the consumer                                      | 1                                     |
| ACSP_PROFILE_DELTA_TOPIC      | The topic ID for acsp profile delta kafka topic                                               | acsp-profile-delta                    |
| GROUP_ID                      | The group ID for the service's Kafka topics                                                   | acsp-profile-delta-consumer           |
| MAX_ATTEMPTS                  | The number of times a message will be retried before being moved to the error topic           | 5                                     |
| BACKOFF_DELAY                 | The incremental time delay between message retries                                            | 100                                   |
| LOGLEVEL                      | The level of log messages output to the logs                                                  | debug                                 |
| HUMAN_LOG                     | A boolean value to enable more readable log messages                                          | 1                                     |
| PORT                          | The port at which the service is hosted in ECS                                                | 8080                                  |

## Other useful information

### Error handling

The table below describes the topic a Kafka message is published to when an API error response is received, given the
number of attempts to process that message. The number of attempts is incremented when processed from the main or
retry topic. Any runtime exceptions thrown during the processing of a message are handled by publishing the message
immediately to the <br>`acsp-profile-delta-acsp-profile-delta-consumer-invalid` topic and are not retried.

| API Response | Attempt          | Topic published to                                     |
|--------------|------------------|--------------------------------------------------------|
| 2xx          | any              | _does not republish_                                   |
| 400 or 409   | any              | acsp-profile-delta-acsp-profile-delta-consumer-invalid |
| 4xx or 5xx   | < max_attempts   | acsp-profile-delta-acsp-profile-delta-consumer-retry   |
| 4xx or 5xx   | \>= max_attempts | acsp-profile-delta-acsp-profile-delta-consumer-error   |
