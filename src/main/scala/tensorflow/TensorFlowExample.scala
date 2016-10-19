package tensorflow

import java.io.File
import java.nio.file.{Paths, Files}
import scala.collection.JavaConversions._

object TensorFlowExample {

  def main(args: Array[String]) {

    val jpgFile = args(0)

    if (! new File("model/imagenet_synset_to_human_label_map.txt").exists() ||
      ! new File("model/imagenet_2012_challenge_label_map_proto.pbtxt").exists() ||
      ! new File("model/classify_image_graph_def.pb").exists()) {
      println("run (cd model && sh download.sh)")
      return
    }

    val labelMap = Files.readAllLines(Paths.get("model/imagenet_synset_to_human_label_map.txt"))
      .map(_.split("\\s+", 2))
      .map { case Array(s, l) => (s.trim, l.trim) }
      .toMap

    val indexToString = Files.readAllLines(Paths.get("model/imagenet_2012_challenge_label_map_proto.pbtxt"))
      .dropWhile(_.trim.startsWith("#"))
      .grouped(4)
      .map { grouped =>
        val targetClass = grouped(1).split(":")(1).trim.toInt
        val targetClassString = grouped(2).split(":")(1).trim.stripPrefix("\"").stripSuffix("\"")
        (targetClass, labelMap(targetClassString))
      }
      .toMap

    TensorFlow.using("model/classify_image_graph_def.pb") { tf =>
      val bytes = Files.readAllBytes(Paths.get(jpgFile))
      val inputLayer = "DecodeJpeg/contents:0"
      val outputLayer = "softmax:0"
      tf.run(inputLayer, outputLayer, bytes)
        .zipWithIndex
        .sortBy(-_._1)
        .take(10)
        .foreach { case (score, i) =>
          val humanString = indexToString.getOrElse(i, s"$i")
          println(f"$humanString (score = $score%.5f)")
        }
    }

  }

}
