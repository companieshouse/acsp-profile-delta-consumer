#!/bin/bash

PORT=8080

exec java -jar -Dserver.port="${PORT}" "acsp-profile-delta-consumer.jar"