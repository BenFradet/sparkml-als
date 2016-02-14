#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd ${DIR}

spark-submit \
  --master local[2] \
  src/main/python/als_example.py
