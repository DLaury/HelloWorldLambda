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