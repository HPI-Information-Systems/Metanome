trap 'kill %1' SIGINT
java -Xmx2g -jar webapp-runner.jar backend --port 8081  &
java -Xmx2g -jar webapp-runner.jar frontend