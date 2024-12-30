#!/bin/bash

source env/eureka_config.sh

ACTUATOR_URL="http://127.0.0.1:${SERVICE_PORT}/actuator/serviceregistry"
ip=`hostname -I | awk '{print $1}'`
INSTANCE_ID="${SERVICE_NAME}":"${ip}":"${SERVICE_PORT}"

echo "================eureka config=========="
echo -e "server name: [ ${SERVICE_NAME} ]"
echo -e "server actuator url: [ ${ACTUATOR_URL} ]"
echo -e "server instance info: [ ${INSTANCE_ID} ]"


EUREKA_SERVICE_URL="http://${eureka_user_name}:${eureka_user_password}@${eureka_first_address}:8761/eureka/apps/"

echo "Sending graceful shutdown request..."
HTTP_CODE=$(curl -o /dev/null -s -w "%{http_code}\n" -m 3 -X POST "${ACTUATOR_URL}?status=DOWN" -H "Content-Type:application/json")
if [ $HTTP_CODE -ne 200 ]; then
  echo "Failed to deregister from service self. try to using eureka service.."
  HTTP_CODE=$(curl -o /dev/null -s -w "%{http_code}\n" -m 3 -X PUT "${EUREKA_SERVICE_URL}/${SERVICE_NAME}/${INSTANCE_ID}/status?value=DOWN")
  if [ $HTTP_CODE -ne 200 ]; then
    echo "Failed to deregister from eureka service."
    exit 1
  else
    echo "Successful deregister from eureka."
  fi
else
  echo "Successful deregister from service registry via actuator url"
fi

sleep 5

echo "graceful shutdown prestop."