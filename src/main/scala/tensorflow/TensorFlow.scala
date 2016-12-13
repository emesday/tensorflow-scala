package tensorflow

class TensorFlow(modelPath: String, maxSize: Int) {

  def this(modelPath: String) = this(modelPath, 2048)

  private var session = TensorFlowNative.tensorFlowNative.tfCreateSession(modelPath)

  def reload(modelPath: String): Unit = {
    val oldSession = session
    session = TensorFlowNative.tensorFlowNative.tfCreateSession(modelPath)

    // cleanup after 10 sec.
    new Thread(new Runnable {
      override def run(): Unit = {
        Thread.sleep(10000)
        TensorFlowNative.tensorFlowNative.tfCloseSession(oldSession)
      }
    }).start()
  }

  def run(inputLayer: String, outputLayer: String, data: Array[Byte]): Array[Float] =
    run(inputLayer, outputLayer, data, -1)

  def run(inputLayer: String, outputLayer: String, data: Array[Byte], expectedOutputSize: Int): Array[Float] = {
    val result = new Array[Float](if(expectedOutputSize == -1) maxSize else expectedOutputSize)
    val outputSize = TensorFlowNative.tensorFlowNative.tfRunString(session, inputLayer, outputLayer, data, data.length, result)
    if (expectedOutputSize == -1) result.take(outputSize) else result
  }

  def run(inputLayer: String, outputLayer: String, data: Array[Float]): Array[Float] =
    run(inputLayer, outputLayer, data, -1)

  def run(inputLayer: String, outputLayer: String, data: Array[Float], expectedOutputSize: Int): Array[Float] = {
    val result = new Array[Float](if(expectedOutputSize == -1) maxSize else expectedOutputSize)
    val outputSize = TensorFlowNative.tensorFlowNative.tfRunFloatArray(session, inputLayer, outputLayer, data, data.length, result)
    if (expectedOutputSize == -1) result.take(outputSize) else result
  }

  def close(): Unit = {
    TensorFlowNative.tensorFlowNative.tfCloseSession(session)
  }

}

object TensorFlow {

  private def using[A, B <: { def close(): Unit }](closeable: B)(f: B => A): A = {
    try {
      f(closeable)
    } finally {
      if (closeable != null) {
        closeable.close()
      }
    }
  }

  def using[T](modelPath: String)(f: TensorFlow => T): T =
    using(new TensorFlow(modelPath))(f)

  def using[T](modelPath: String, maxSize: Int)(f: TensorFlow => T): T =
    using(new TensorFlow(modelPath, maxSize))(f)

}


