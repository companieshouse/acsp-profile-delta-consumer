# acsp-profile-delta-consumer

## Summary

## Environment variables

| Variable                      | Description                                                                           | Example (from docker-chs-development) |
|-------------------------------|---------------------------------------------------------------------------------------|-------------------------------------|
| ACSP_PROFILE_API_KEY          | The client ID of an API key, with internal app privileges, to call ascp-data-api with | abc123def456ghi789                  |
| API_LOCAL_URL                 | The host through which requests to the acsp-profile-data-api are sent                 | http://api.chs.local:4001           |
| BOOTSTRAP_SERVER_URL          | The URL to the kafka broker                                                           | kafka:9092                          |
| CONCURRENT_LISTENER_INSTANCES | The number of listeners run in parallel for the consumer                              | 1                                   |
| ACSP_PROFILE_DELTA_TOPIC      | The topic ID for acsp profile delta kafka topic                                       | acsp-profile-delta                  |
| GROUP_ID                      | The group ID for the service's Kafka topics                                           | acsp-profile-delta-consumer       |
| MAX_ATTEMPTS                  | The number of times a message will be retried before being moved to the error topic   | 5                                   |
| BACKOFF_DELAY                 | The incremental time delay between message retries                                    | 100                                 |
| LOGLEVEL                      | The level of log messages output to the logs                                          | debug                               |
| HUMAN_LOG                     | A boolean value to enable more readable log messages                                  | 1                                   |
| PORT                          | The port at which the service is hosted in ECS                                        | 8080                                |
