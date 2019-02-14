#!/bin/bash

#ENVIRONMENT_PROFILE=$([ ".${environment}" == "." ] && echo "dev" || echo "${environment}")


java \
    -XX\:+UnlockExperimentalVMOptions \
    -XX:+UseCGroupMemoryLimitForHeap \
    -jar proxy-service-1.0.0.0.jar
