#!/bin/bash

INFO_TEXT="This is simple install script to setap environment for ArtificialNeuralNetworkModule of Ascle system. The purpose of this program is to clasiffy Heart Disease"
INFO_TEXT2="Script will set up directories, perform connection test and if required train ANN"

echo $INFO_TEXT
echo $INFO_TEXT2

echo Checking for Java
if type -p java; then
    echo found java executable in PATH
    _java=java
    echo `"$_java" -version`
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
    echo `"$_java" -version`
else
    echo "no java"
    exit -1
fi

echo setting up directory
mkdir -v dataFiles
mkdir -v dataFiles/backup
echo unpacking data files and .jar file
tar -xf data.tar -C dataFiles
#move saved ann to backup
tar -xf prog.jar

echo performing connection test
#test here
