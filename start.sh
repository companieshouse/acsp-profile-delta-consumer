#!/bin/bash

APP_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [[ -z "${MESOS_SLAVE_PID}" ]]; then
  echo "This script must be run from within Mesos" >&2
  exit 1
fi

PORT="$1"
CONFIG_URL="$2"
ENVIRONMENT="$3"
APP_NAME="$4"

source /etc/profile

echo "Downloading environment from: ${CONFIG_URL}/${ENVIRONMENT}/${APP_NAME}"
wget -O "${APP_DIR}/private_env" "${CONFIG_URL}/${ENVIRONMENT}/private_env"
wget -O "${APP_DIR}/global_env" "${CONFIG_URL}/${ENVIRONMENT}/global_env"
wget -O "${APP_DIR}/app_env" "${CONFIG_URL}/${ENVIRONMENT}/${APP_NAME}/env"
source "${APP_DIR}/private_env"
source "${APP_DIR}/global_env"
source "${APP_DIR}/app_env"

exec java ${JAVA_MEM_ARGS} -jar -Dserver.port="${PORT}" "${APP_DIR}/acsp-profile-delta-consumer.jar"