#!/bin/sh

# Created by realsnake1975@gmail.com

ps -ef | grep uhdb-server-1.0 | grep -v grep | awk '{print "kill -9 " $2}' | sh -x
#sleep 120

#JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:MaxNewSize=384m -XX:PermSize=128m -XX:MaxPermSize=128m -XX:ParallelGCThreads=2 -XX:-UseConcMarkSweepGC -Dspring.profiles.active=real"

#java $JAVA_OPTS -jar /data/servers/uhdb-server-1.0.jar &
