val sparkVersion = "1.6.0"
lazy val root = (project in file("."))
  .settings(
    organization := "io.github.benfradet",
    name := "sparkml-als",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-mllib" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion
    )
  )