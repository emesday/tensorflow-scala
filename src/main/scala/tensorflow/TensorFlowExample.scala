package tensorflow

import java.awt.image.DataBufferByte
import java.io.{BufferedInputStream, ByteArrayOutputStream}
import java.net.URL
import java.nio.file.{Files, Paths}
import javax.imageio.ImageIO

import tensorflow.model.InceptionV3

object TensorFlowExample {

  def main(args: Array[String]) {
    // image file
    val jpgFile = args.headOption.getOrElse("cropped_panda.jpg")
    val jpgAsBytes = jpgFile match {
      case urlString if urlString.startsWith("http") =>
        val url = new URL(urlString)
        val in = new BufferedInputStream(url.openStream())
        val out = new ByteArrayOutputStream()
        val buf = new Array[Byte](1024)
        var n = in.read(buf)
        while (n != -1) {
          out.write(buf, 0, n)
          n = in.read(buf)
        }
        val bytes = out.toByteArray
        out.close()
        in.close()
        bytes
      case file => Files.readAllBytes(Paths.get(file))
    }

    // define the model
    val model = new InceptionV3("model")

    // initialize TensorFlowProvider
    val provider = new TensorFlowProvider(model)

    // setting up input and output layers to classify
    val inputLayer = "DecodeJpeg/contents"
    val outputLayer = "softmax"

    // get result of the outputLayer
    val result = provider.run(inputLayer -> jpgAsBytes, outputLayer)

    // get label of the top 5
    val label = model.getLabelOf(result.head, 5)

    // print out
    label foreach println

    // shows ...
    //
    // Label(n02510455,giant panda, panda, panda bear, coon bear, Ailuropoda melanoleuca,0.8910737)
    // Label(n02500267,indri, indris, Indri indri, Indri brevicaudatus,0.007790538)
    // Label(n02509815,lesser panda, red panda, panda, bear cat, cat bear, Ailurus fulgens,0.0029591226)
    // Label(n07760859,custard apple,0.0014657712)
    // Label(n13044778,earthstar,0.0011742385)

    // release resources
    provider.close()
  }

}
