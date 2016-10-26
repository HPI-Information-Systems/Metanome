#!/bin/bash
java -Xmx2g -jar webapp-runner.jar backend --port 8081  &
java -Xmx2g -jar webapp-runner.jar frontend && fg
