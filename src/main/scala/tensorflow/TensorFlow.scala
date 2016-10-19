package tensorflow

class TensorFlow(modelPath: String, labelPath: String, codePath: String) {

  def this(modelPath: String) = this(modelPath, null, null)

  private val session = TensorFlowNative.tensorFlowNative.tfCreateSession(modelPath)

  private val labels = if (labelPath == null) {
    scala.io.Source.fromFile(labelPath).getLines().toArray
  } else {
    Array.empty[String]
  }

  private val codes = if (codePath == null) {
    scala.io.Source.fromFile(codePath).getLines().toArray
  } else {
    Array.empty[String]
  }

  def getLabels: Array[String] = labels

  def getCodes: Array[String] = codes

  def run(inputLayer: String, outputLayer: String, data: Array[Byte], size: Int, expectedSize: Int): Array[Float] = {
    val result = new Array[Float](if(expectedSize == -1) 2048 else expectedSize)
    val outputSize = TensorFlowNative.tensorFlowNative.tfRunString(session, inputLayer, outputLayer, data, size, result)
    if (expectedSize == -1) result.take(outputSize) else result
  }

  def run(inputLayer: String, outputLayer: String, data: Array[Float], size: Int, expectedSize: Int): Array[Float] = {
    val result = new Array[Float](if(expectedSize == -1) 2048 else expectedSize)
    val outputSize = TensorFlowNative.tensorFlowNative.tfRunFloatArray(session, inputLayer, outputLayer, data, size, result)
    if (expectedSize == -1) result.take(outputSize) else result
  }

  def classify(inputLayer: String, outputLayer: String, data: Array[Byte], size: Int, expectedSize: Int): Array[(String, String, Float)] = {
    run(inputLayer, outputLayer, data, size, expectedSize).zip(labels).zip(codes).map { x =>
      (x._2, x._1._2, x._1._1)
    }
  }

  def classify(inputLayer: String, outputLayer: String, data: Array[Float], size: Int, expectedSize: Int): Array[(String, String, Float)] = {
    run(inputLayer, outputLayer, data, size, expectedSize).zip(labels).zip(codes).map { x =>
      (x._2, x._1._2, x._1._1)
    }
  }

  def close(): Unit = {
    TensorFlowNative.tensorFlowNative.tfCloseSession(session)
  }

}

object TensorFlow {

  def withTensorFlow[T](modelPath: String, labelPath: String, codePath: String)(f: TensorFlow => T): T = {
    val tensorflow = new TensorFlow(modelPath, labelPath, codePath)
    try {
      f(tensorflow)
    } finally {
      if (tensorflow != null) {
        tensorflow.close()
      }
    }
  }

}


