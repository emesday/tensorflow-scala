[![Analytics](https://ga-beacon.appspot.com/UA-98128111-1/tensorflow-scala)](https://github.com/igrigorik/ga-beacon)

# A Scala binding of TensorFlow for Serving TensorFlow Models

```
$ git clone https://github.com/mskimm/tensorflow-scala.git
$ cd tensorflow-scala
$ (cd model; sh download.sh)
$ sbt "run-main tensorflow.TensorFlowExample cropped_panda.jpg"
or
$ sbt "run-main tensorflow.TensorFlowExample /path/to/jpg/image"
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
