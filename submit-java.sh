#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}

sbt clean package
spark-submit \
  --class io.github.benfradet.sparkml.als.JavaALSExample \
  --master local[2] \
  target/scala-2.10/sparkml-als_2.10-0.1.jar