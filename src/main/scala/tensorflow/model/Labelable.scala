package tensorflow.model

case class Label(
  code: String,
  label: String,
  score: Float
)

trait Labelable {

  def getLabelOf(tensor: Array[Float], limit: Int = 10): Seq[Label]

}
