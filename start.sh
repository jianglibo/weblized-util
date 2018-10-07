#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
cd $DIR
echo "in working directory: $DIR"
JAR=$(find serverWorkingDir -name "*.jar")
echo "find jar $JAR Starting..."

java -jar $JAR