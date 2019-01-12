#!/usr/bin/env bash

APP_NAME=basic-auth

tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
        echo 'App is running.'
else
        echo 'App is NOT running.'
fi