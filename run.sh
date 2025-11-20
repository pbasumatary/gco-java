#!/bin/bash
set -e

echo "Compiling..."
mkdir -p out
javac $(find src/main/java -name "*.java") -d out

echo "Starting web server..."
java -cp out com.codeoff.WebServer

