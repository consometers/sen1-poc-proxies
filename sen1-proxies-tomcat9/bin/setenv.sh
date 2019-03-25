#!/bin/sh

export CATALINA_PID="/var/run/serverId.pid"

export JAVA_OPTS="-Duser.language=fr \
-Duser.region=FR \
-Duser.timezone=Europe/Paris \
-Duser.encoding=UTF-8 \
-Dfile.encoding=UTF-8 \
"

export CATALINA_OPTS="-Xmx256M -Xms256M \
-Djava.awt.headless=true \
-XX:OnOutOfMemoryError=\"kill -9 %p\" \
"

