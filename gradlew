#!/bin/sh

##############################################################################
#  Gradle wrapper start up script for POSIX
##############################################################################

APP_HOME=$( cd "${0%"${0##*/}"}." && pwd -P ) || exit

JAVACMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"

exec "$JAVACMD" \
  ${DEFAULT_JVM_OPTS:+"$DEFAULT_JVM_OPTS"} \
  ${JAVA_OPTS:+"$JAVA_OPTS"} \
  ${GRADLE_OPTS:+"$GRADLE_OPTS"} \
  "-Dorg.gradle.appname=${0##*/}" \
  -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
  org.gradle.wrapper.GradleWrapperMain \
  "$@"
