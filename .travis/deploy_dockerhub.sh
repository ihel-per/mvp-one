#!/bin/sh
docker login -u $DOCKER_USER -p $DOCKER_PASS
docker build -f Dockerfile -t ihelper/ihelper:latest .
docker push ihelper/ihelper:latest_1
