# HelloWorldLambda
Lambda function that accesses JSON and prints out a greeting using payload

### Basic Hello World with Test with sbt
- Create directory and put build.sbt file in directory
  -  `mkdir lambda-demo`
  -  `cd lambda-demo`
  -  `touch build.sbt`
- Start sbt shell
  -  `sbt`
  -  (exiting the shell: exit)
- Compile project
  - `compile`
  - `~compile` will cause compile to happen on code change
- Create directory src/main/scala/example
  -  `mkdir src/main/scala/example` (\ for command prompt)
- Navigate to scala folder
  -  `cd src/main/scala/example` (\ for command prompt)
- Create and open main file Hello.scala
  -  Using notepad++ - `start notepad++ Hello.scala`
- Edit file to include following text
```scala
package example

object Hello {
	def main(args: Array[String]): Unit = {
	    println("Hello")
	}
}
```
- If not running ~compile run compile again
  -  `compile`
- Run the program
  -  `run`
- Set ThisBuild / scalaVersion
  -  `set ThisBuild / scalaVersion := "2.13.6"` (or whatever version you are using)
- Save ad-hoc settings to build.sbt
  -  `session save`
  -  can set to automatically reload build when changes are detected with following:
  -  `set Global / onChangedBuildSource := ReloadOnSourceChanges`
- Rename project
  -  Add following to build.sbt
```scala
lazy val hello = (project in file("."))
        .settings(
	      name := "Hello"
        )
```
  -  `reload`
  -  command prompt should now say `sbt:Hello>`
- Add ScalaTest to dependencies
  -  Add following to build.sbt under name:= "Hello",
  -  `libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test`
  -  `reload`
- Running tests
  -  `test`
  -  `~testQuick` - will continuously run tests
- Write test
  -  `cd src`
  -  `mkdir test/scala` (\ for command prompt)
  -  `start notepad++ HelloSpec.scala`
```scala
import org.scalatest.funsuite._

class HelloSpec extends AnyFunSuite {
    test("Hello should start with H") {
        assert("Hello".startsWith("H"))
    }
}
```
  -  if running ~testQuick test should pass

### Updating for Lambda function
- Add sbtPlugin sbt-assembly for easier deployment
  -   I would like to investigate this more but I am following these instructions
  -  https://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/
  -  `cd ../../../../project`
  -  `start notepad++ plugins.sbt`
  -  add the following:
  -  `addSbtPlugin("com.edd3si9n" % "sbt-assembly" % "1.2.0")`
  -  `reload`
- Update build.sbt file
- - Update to the following:
```scala
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
    libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.13.2"
    )

ThisBuild / assemblyMergeStrategy :=
    {
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x => MergeStrategy.first
    }
}
```
- Update Hello.scala file
```scala
package example
    
import com.amazonaws.services.lambda.runtime.{ Context, RequestStreamHandler }

case class NameInfo(firstName: String, lastName: String)

class Hello extends RequestStreamHandler {
  import java.io.{InputStream, OutputStream, PrintStream}

  val scalaMapper = {
    import com.fasterxml.jackson.databind.ObjectMapper
    import com.fasterxml.jackson.module.scala.DefaultScalaModule
    new ObjectMapper().registerModule(new DefaultScalaModule)
}

def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val name = scalaMapper.readValue(input, classOf[NameInfo])
    val result = s"Greetings ${name.firstName} ${name.lastName}." 
    output.write(result.getBytes("UTF-8"))
    }
}
```
- Compile new code
  -  `compile`
- Assemble jar
  -  `assembly`
- Publish jar to AWS Lambda
  -  located at target/scala-2.13/Hello-assembly-1.0.jar
- Use with hello-world template with following JSON
```JSON
{
    "firstName": "Dan",
    "lastName": "Laury"
}
```
