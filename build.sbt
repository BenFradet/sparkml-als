val sparkVersion = "1.6.0"
lazy val root = (project in file("."))
  .settings(
    organization := "io.github.benfradet",
    name := "sparkml-als",
    version := "0.1",
    scalaVersion := "2.10.5",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
    )
  )