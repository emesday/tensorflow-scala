# Scala/Java binding of TensorFlow 

## Scala API 

    import java.nio.file.{Paths, Files}
    val bytes = Files.readAllBytes(Paths.get("/path/to/jpg"))
    val inputLayer = "DecodeJpeg/contents:0"
    val outputLayer = "softmax:0"
    val result: Array[Float] = TensorFlow.using("/path/to/model") { tf =>
       tf.run(inputLayer, outputLayer, bytes)
    }

## Installation
For linux-x86-64 or OSX users,

    resolvers += Resolver.bintrayRepo("mskimm", "maven")
    libraryDependencies += "com.github.mskimm" %% "tensorflow-java" % "0.0.1"

## Building the native TensorFlow library
Or,

    $ git clone https://github.com/mskimm/tensorflow-java.git
    $ cd tensorflow-java
    $ sbt compileNative # invoke bazel, see https://www.tensorflow.org/versions/master/get_started/os_setup.html
    $ sbt package
    
## Run

    $ (cd model && sh download.sh)
    $ sbt "runMain tensorflow.TensorFlowExample cropped_panda.jpg"
  
where `cropped_panda.jpg` is:

![cropped_panda](https://raw.githubusercontent.com/mskimm/tensorflow-java/master/cropped_panda.jpg)

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
