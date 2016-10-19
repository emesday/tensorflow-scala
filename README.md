# Java binding of TensorFlow 

## Build

    $ git clone https://github.com/mskimm/tensorflow-java.git
    $ cd tensorflow-java
    $ sbt compileNative # invoke bazel, see https://www.tensorflow.org/versions/master/get_started/os_setup.html
    $ sbt package
    
## Run

    $ (cd model && sh download.sh)
    $ sbt "runMain tensorflow.TensorFlowExample cropped_panda.jpg"
    
This will show

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

