#!/bin/sh

##############################################################################
#  Gradle wrapper start up script for POSIX
##############################################################################

# Determine APP_HOME
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=$(ls -ld "$PRG")
    link=$(expr "$ls" : '.*-> \(.*\)$')
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=$(dirname "$PRG")/"$link"
    fi
done
APP_HOME=$(dirname "$PRG")
APP_HOME=$(cd "$APP_HOME" && pwd -P) || exit

# Determine Java command
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

# Execute Gradle
exec "$JAVACMD" \
  $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
  "-Dorg.gradle.appname=$(basename "$0")" \
  -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
