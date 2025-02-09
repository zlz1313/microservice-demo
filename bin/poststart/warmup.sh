#!/bin/bash

# 服务健康检查URL
HEALTH_CHECK_URL="http://localhost:8080/actuator/health/liveness"
# 要请求的API URL
API_URL="http://localhost:8080/apiget"
# 请求次数
REQUEST_COUNT=10
# 请求间隔（秒）
REQUEST_INTERVAL=5
# 健康检查超时时间（秒）
HEALTH_CHECK_TIMEOUT=60
# 健康检查超时时间（秒）
INIT_WAIT_CHECK_TIME=60

echo "Waiting for the application to start successfully..."
sleep $INIT_WAIT_CHECK_TIME

health_check_attempts=0
until curl -s -o /dev/null -m 2 -w "%{http_code}" $HEALTH_CHECK_URL | grep -q "200" || [ $health_check_attempts -ge $((HEALTH_CHECK_TIMEOUT / $REQUEST_INTERVAL)) ]; do
  health_check_attempts=$((health_check_attempts + 1))
  # 如果达到最大尝试次数，则退出循环
  if [ $health_check_attempts -ge $((HEALTH_CHECK_TIMEOUT / $REQUEST_INTERVAL)) ]; then
      echo "The application health check failed due to timeout!"
      exit 1
  fi
  sleep $REQUEST_INTERVAL;
done;

# 健康检查通过，开始发送API请求
echo "Service is healthy. Starting warm up APIs..."
for i in $(seq 1 $REQUEST_COUNT); do
  # 发送API请求
  curl -s $API_URL

  # 等待下一个请求间隔
  sleep $REQUEST_INTERVAL
done

echo "Warm Up service done."