[![Build Status](https://travis-ci.org/mskimm/tensorflow-scala.svg?branch=master)](https://travis-ci.org/mskimm/tensorflow-scala)

# A Scala binding of TensorFlow for Serving TensorFlow Models

## Quick Start
```
$ git clone https://github.com/mskimm/tensorflow-scala.git
$ cd tensorflow-scala
$ (cd model; sh download.sh) # download inception-v3
$ sbt "run-main tensorflow.TensorFlowExample cropped_panda.jpg"
or
$ sbt "run-main tensorflow.TensorFlowExample /path/to/jpg/image"
or
$ sbt "run-main tensorflow.TensorFlowExample http://path/to/online/image"
```

where `cropped_panda.jpg` is:

![cropped_panda](https://raw.githubusercontent.com/mskimm/tensorflow-scala/master/cropped_panda.jpg)

The `sbt "run-main tensorflow.TensorFlowExample cropped_panda.jpg"` will show:

```
Label(n02510455,giant panda, panda, panda bear, coon bear, Ailuropoda melanoleuca,0.8910737)
Label(n02500267,indri, indris, Indri indri, Indri brevicaudatus,0.007790538)
Label(n02509815,lesser panda, red panda, panda, bear cat, cat bear, Ailurus fulgens,0.0029591226)
Label(n07760859,custard apple,0.0014657712)
Label(n13044778,earthstar,0.0011742385)
```

## Example

See https://github.com/mskimm/tensorflow-scala/blob/master/src/main/scala/tensorflow/TensorFlowExample.scala

# History
 - 16 June 2017 - Changed the objectives of this project to `Serving TensorFlow Models` with Scala
 - 11 May 2017 - Decided to deprecate this project because TensorFlow provides Java API from the version 1.0.0.
 - 19 Oct 2016 - This project started with JNA binding of TensorFlow.

 
