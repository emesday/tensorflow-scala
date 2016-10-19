import com.sun.jna.Platform

val compileNative = taskKey[Unit]("Compile tensorflow into shared library.")

name := "tensorflow"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6")

libraryDependencies ++= Seq(
  "net.java.dev.jna" % "jna" % "4.2.2"
)

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
  "cp -r cd/main/cpp/* tensorflow/tensorflow/jna/".!
  Process("bazel build -c opt //tensorflow:libtensorflow_cc.so", new File("tensorflow")).!
  s"rm -f $lib".!
  s"mv tensorflow/bazel-bin/tensorflow/libtensorflow_cc.so $lib".!
}
