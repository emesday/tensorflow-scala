[![Analytics](https://ga-beacon.appspot.com/UA-98128111-1/tensorflow-scala)](https://github.com/igrigorik/ga-beacon)

# Notice

Please, use [TensorFlow Java API](https://www.tensorflow.org/api_docs/java/reference/org/tensorflow/package-summary) instead.

# Tutorial of TensorFlow Java API

1. Add the dependencies to `build.sbt`
```
libraryDependencies ++= Seq(
  "org.tensorflow" % "libtensorflow" % "1.1.0", // Java binding
  "org.tensorflow" % "libtensorflow_jni" % "1.1.0" // Native Libraries for OSX, Linux, and Windows
)
```
1. Create `session`
```
import java.nio.file.{Files, Paths}
import org.tensorflow.{Graph, Session, Tensor}

val modelPath = "/path/to/modelPath"
val graphDef = Files.readAllBytes(Paths.get(modelPath))
val graph = new Graph()
graph.importGraphDef(graphDef)
val session = new Session(graph)
```
1. Get `runner`
```
// cont'd
val runner = session.runner()
```
1. Create an input `tensor` and `feed`
```
// cont'd
val data: Any = _ // Array[Byte], Array[Float] ...
val tensor = Tensor.create(data)
runner.feed("some/operation", tensor)
```
1. Get `output` of the `layer`
```
// cont'd
val outputLayer: String = "some/layer"
val output = runner.fetch(outputLayer).run()
```

## (deprecated) A Scala binding of TensorFlow 

https://www.tensorflow.org/versions/r0.11/tutorials/image_recognition/index.html

### Scala API 

    import java.nio.file.{Paths, Files}
    val bytes = Files.readAllBytes(Paths.get("/path/to/jpg"))
    val inputLayer = "DecodeJpeg/contents:0"
    val outputLayer = "softmax:0"
    val result: Array[Float] = TensorFlow.using("/path/to/model") { tf =>
       tf.run(inputLayer, outputLayer, bytes)
    }

### Installation
For linux-x86-64 or OSX users,

    resolvers += Resolver.bintrayRepo("mskimm", "maven")
    libraryDependencies += "com.github.mskimm" %% "tensorflow-scala" % "0.0.1"

### Building the native TensorFlow library
Or,

    $ git clone https://github.com/mskimm/tensorflow-scala.git
    $ cd tensorflow-scala
    $ sbt compileNative # invoke bazel, see https://www.tensorflow.org/versions/master/get_started/os_setup.html
    $ sbt package
    
### Run

    $ (cd model && sh download.sh)
    $ sbt "runMain tensorflow.TensorFlowExample cropped_panda.jpg"
  
where `cropped_panda.jpg` is:

![cropped_panda](https://raw.githubusercontent.com/mskimm/tensorflow-scala/master/cropped_panda.jpg)

The last command will show:

    giant panda, panda, panda bear, coon bear, Ailuropoda melanoleuca (score = 0.89233)
    indri, indris, Indri indri, Indri brevicaudatus (score = 0.00859)
    lesser panda, red panda, panda, bear cat, cat bear, Ailurus fulgens (score = 0.00264)
    custard apple (score = 0.00141)
    earthstar (score = 0.00107)
    sea urchin (score = 0.00080)
    forklift (score = 0.00052)
    soccer ball (score = 0.00047)
    go-kart (score = 0.00047)
    digital watch (score = 0.00046)
