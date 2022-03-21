javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

ThisBuild / scalaVersion := "2.13.6"

lazy val hello = (project in file("."))
    .settings(
	    name := "Hello",
		version := "1.0",
		scalaVersion := "2.13.6",
		retrieveManaged := true,
		libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test,
		libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.2.1",
		libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "3.11.0",
		libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.2"

	)

ThisBuild / assemblyMergeStrategy := { 
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}