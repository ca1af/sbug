!#/bin/bash

ps -ef | grep 'java -jar build/libs/sbug-0.0.1-SNAPSHOT.jar' | awk '{print $2}' | while read line; do kill $line; done

nohup java -jar build/libs/*.jar > /dev/null 2> /dev/null < /dev/null &