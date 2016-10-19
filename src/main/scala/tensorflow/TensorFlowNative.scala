package tensorflow


import com.sun.jna._

trait TensorFlowNative extends Library {

  def tfCreateSession(modelPath: String): Pointer

  def tfRunString(session: Pointer, inputLayer: String, outputLayer: String, data: Array[Byte], size: Int, result: Array[Float]): Int

  //def tfGetOutputSize(session: Pointer, outputLayer: String): Int

  def tfRunFloatArray(session: Pointer, inputLayer: String, outputLayer: String, data: Array[Float], size: Int, result: Array[Float]): Int

  def tfCloseSession(session: Pointer): Unit

}

object TensorFlowNative {
  val tensorFlowNative = Native.loadLibrary("tensorflow", classOf[TensorFlowNative]).asInstanceOf[TensorFlowNative]
}

