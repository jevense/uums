#!/usr/bin/env bash

rm -f tpid
nohup /data/servers/jdk1.8.0_121/bin/java -jar basic-auth-1.0-SNAPSHOT.jar -Dspring.profiles.active=test > /dev/null 2>&1 &
echo $! > tpid
echo Start Success!