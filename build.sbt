import com.sun.jna.Platform

val compileNative = taskKey[Unit]("Compile tensorflow into shared library.")

name := "tensorflow-scala"

version := "0.0.3-SNAPSHOT"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6")

libraryDependencies ++= Seq(
  "net.java.dev.jna" % "jna" % "4.2.2"
)

organization := "com.github.mskimm"

licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")

compileNative := {
  import scala.sys.process._
  val libDir = file(s"src/main/resources/${Platform.RESOURCE_PREFIX}")
  if (!libDir.exists) {
    libDir.mkdirs()
  }
  val lib = if (Platform.RESOURCE_PREFIX == "darwin") {
    libDir / "libtensorflow.dylib"
  } else {
    libDir / "libtensorflow.so"
  }
  Process("git checkout v0.10.0", new File("tensorflow")).!
  "mkdir -p tensorflow/tensorflow/jna".!
  "cp src/main/cpp/BUILD tensorflow/tensorflow/jna/".!
  "cp src/main/cpp/jna.cc tensorflow/tensorflow/jna/".!
  Process("bazel build -c opt //tensorflow/jna:libtensorflow.so", new File("tensorflow")).!
  s"rm -f $lib".!
  s"mv tensorflow/bazel-bin/tensorflow/jna/libtensorflow.so $lib".!
}


