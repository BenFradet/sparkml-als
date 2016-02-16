# sparkml-als
Alternating least squares in spark.ml

This repository contains the code related to
[the blog post documenting ALS in spark.ml](http://benfradet.github.io/blog/2016/02/15/Alernating-least-squares-and-collaborative-filtering-in-spark.ml).

There are examples for the 3 languages supported by Spark:
  - [ALSExample.scala](src/main/scala/io/github/benfradet/sparkml/als/ALSExample.scala)
  - [JavaALSExample.java](src/main/java/io/github/benfradet/sparkml/als/JavaALSExample.java)
  - [als_example.py](src/main/python/als_example.py)
  
To run one of them, refer to the appropriate script:
  - [submit-scala.sh](submit-scala.sh) for Scala
  - [submit-java.sh](submit-java.sh) for Java
  - [submit-python.sh](submit-python.sh) for Python
