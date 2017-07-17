package tensorflow.model

import java.nio.charset.Charset
import java.nio.file.{Files, Paths}

import scala.collection.JavaConverters._

class InceptionV3(graphPath: String, humanLabelPath: String, labelMapPath: String) extends TensorFlowModel with Labelable {

  def this(modelPath: String) = this(
    s"$modelPath/classify_image_graph_def.pb",
    s"$modelPath/imagenet_synset_to_human_label_map.txt",
    s"$modelPath/imagenet_2012_challenge_label_map_proto.pbtxt")

  private val codeLabelSeq: Array[(String, String)] = {
    val labelMap = Files.readAllLines(Paths.get(humanLabelPath), Charset.defaultCharset()).asScala
      .map(_.split("\\s+", 2))
      .map { case Array(s, l) => (s.trim, l.trim) }
      .toMap

    val indexToCode = Files.readAllLines(Paths.get(labelMapPath), Charset.defaultCharset()).asScala
      .dropWhile(_.trim.startsWith("#"))
      .grouped(4)
      .map { grouped =>
        val targetClass = grouped(1).split(":")(1).trim.toInt
        val targetClassString = grouped(2).split(":")(1).trim.stripPrefix("\"").stripSuffix("\"")
        (targetClass, (targetClassString, labelMap(targetClassString)))
      }
      .toMap
      .withDefault(_ => "" -> "")

    Array.tabulate(indexToCode.keys.max + 1)(indexToCode.apply)
  }

  override def getBytes: Array[Byte] =
    Files.readAllBytes(Paths.get(graphPath))

  override def getLabelOf(tensor: Array[Float], limit: Int): Seq[Label] = {
    val all = tensor.zip(codeLabelSeq).map { case (score, (code, label)) =>
      Label(code, label, score)
    }
    all.sortBy(-_.score).take(limit).toSeq
  }

}
