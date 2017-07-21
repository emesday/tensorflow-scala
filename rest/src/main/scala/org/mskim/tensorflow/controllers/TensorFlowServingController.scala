package org.mskim.tensorflow.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.request.RequestUtils
import com.twitter.util.Future
import tensorflow.TensorFlowProvider
import tensorflow.model.InceptionV3

class TensorFlowServingController extends Controller {

  // define the model
  val model = new InceptionV3("model")

  // initialize TensorFlowProvider
  val provider = new TensorFlowProvider(model)

  // setting up input and output layers to classify
  val inputLayer = "DecodeJpeg/contents"
  val outputLayer = "softmax"

  post("/v1/image/label") { request: Request =>
    val multiParams = RequestUtils.multiParams(request)
    multiParams.get("image") match {
      case Some(image) =>
        val result = provider.run(inputLayer -> image.data, outputLayer)
        model.getLabelOf(result.head, 5)
      case None => Future.None
    }
  }

}
