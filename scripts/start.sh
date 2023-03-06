#!/bin/bash

ps -ef | grep 'java -jar /home/ubuntu/github_action/build/libs/sbug-0.0.1-SNAPSHOT.jar' | awk '{print $2}' | while read line; do sudo kill $line; done >/dev/null 2>&1
nohup java -jar /home/ubuntu/github_action/build/libs/*.jar >/dev/null 2>&1 &