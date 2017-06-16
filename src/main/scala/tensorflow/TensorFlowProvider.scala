package tensorflow

import org.tensorflow.{Graph, Session, Tensor}
import tensorflow.model.TensorFlowModel
import scala.collection.JavaConverters._

class TensorFlowProvider(model: TensorFlowModel) extends AutoCloseable {

  private val graph: Graph = {
    val graph = new Graph()
    graph.importGraphDef(model.getBytes)
    graph
  }

  private val session: Session = new Session(graph)

  def run(i1: (String, Any), outputs: String*): Seq[Array[Float]] =
    run(Seq(i1), outputs: _*)

  def run(i1: (String, Any), i2: (String, Any), outputs: String*): Seq[Array[Float]] =
    run(Seq(i1, i2), outputs: _*)

  def run(i1: (String, Any), i2: (String, Any), i3: (String, Any), outputs: String*): Seq[Array[Float]] =
    run(Seq(i1, i2, i3), outputs: _*)

  def run(i1: (String, Any), i2: (String, Any), i3: (String, Any), i4: (String, Any), outputs: String*): Seq[Array[Float]] =
    run(Seq(i1, i2, i3, i4), outputs: _*)

  def run(i1: (String, Any), i2: (String, Any), i3: (String, Any), i4: (String, Any), i5: (String, Any), outputs: String*): Seq[Array[Float]] =
    run(Seq(i1, i2, i3, i4, i5), outputs: _*)

  def run(input: Seq[(String, Any)], output: String*): Seq[Array[Float]] = {
    val runner = session.runner()

    val inputTensors: Seq[(String, Tensor)] = input.map { case (op, obj) =>
      op -> Tensor.create(obj)
    }

    // feed
    inputTensors foreach { case (op, tensor) =>
      runner.feed(op, tensor)
    }

    // fetch
    output foreach runner.fetch

    // run
    val resultTensors: Seq[Tensor] = runner.run().asScala

    val result: Seq[Array[Float]] = resultTensors.map { tensor =>
      val size = tensor.numElements()
      val to = Array(new Array[Float](size))
      tensor.copyTo(to)
      to(0)
    }

    // release
    inputTensors.foreach(_._2.close())
    resultTensors.foreach(_.close())

    result
  }

  override def close(): Unit = {
    session.close()
    graph.close()
  }

}

