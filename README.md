[![Build Status](https://travis-ci.org/mskimm/tensorflow-scala.svg?branch=master)](https://travis-ci.org/mskimm/tensorflow-scala)

# A Scala binding of TensorFlow for Serving TensorFlow Models

## Quick Start
```
$ git clone https://github.com/mskimm/tensorflow-scala.git
$ cd tensorflow-scala
$ (cd model; sh download.sh) # download inception-v3
# start rest server
$ sbt rest/run
```
then post `cropped_panda.jpg` in another terminal using `curl`
```
$ curl -XPOST -F "image=@cropped_panda.jpg" localhost:8888/v1/image/label
```
shows
```
[
  {
    "code": "n02510455",
    "label": "giant panda, panda, panda bear, coon bear, Ailuropoda melanoleuca",
    "score": 0.8910737
  },
  {
    "code": "n02500267",
    "label": "indri, indris, Indri indri, Indri brevicaudatus",
    "score": 0.007790538
  },
  {
    "code": "n02509815",
    "label": "lesser panda, red panda, panda, bear cat, cat bear, Ailurus fulgens",
    "score": 0.0029591226
  },
  {
    "code": "n07760859",
    "label": "custard apple",
    "score": 0.0014657712
  },
  {
    "code": "n13044778",
    "label": "earthstar",
    "score": 0.0011742385
  }
]
```

where `cropped_panda.jpg` is:

![cropped_panda](https://raw.githubusercontent.com/mskimm/tensorflow-scala/master/cropped_panda.jpg)

# History
 - 16 June 2017 - Changed the objectives of this project to `Serving TensorFlow Models` with Scala
 - 11 May 2017 - Decided to deprecate this project because TensorFlow provides Java API from the version 1.0.0.
 - 19 Oct 2016 - This project started with JNA binding of TensorFlow.

 
