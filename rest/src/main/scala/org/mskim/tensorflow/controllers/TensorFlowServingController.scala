package org.mskim.tensorflow.controllers

import java.nio.ByteBuffer

import com.google.common.io.BaseEncoding
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.request.RequestUtils
import com.twitter.util.{Future, FuturePool}
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
  val bottleneckLayer = "pool_3/_reshape"

  post("/v1/image/label") { request: Request =>
    val multiParams = RequestUtils.multiParams(request)
    multiParams.get("image") match {
      case Some(image) =>
        FuturePool.interruptibleUnboundedPool {
          val result = provider.run(inputLayer -> image.data, outputLayer)
          model.getLabelOf(result.head, 5)
        }
      case None => Future.None
    }
  }
  post("/v1/image/features") { request: Request =>
    val reducing = request.getBooleanParam("reduce")
    val multiParams = RequestUtils.multiParams(request)
    multiParams.get("image") match {
      case Some(image) =>
        FuturePool.interruptibleUnboundedPool {
          val features0 = provider.run(inputLayer -> image.data, bottleneckLayer).head
          val features = if (reducing) {
            provider.reduce(features0)
          } else {
            features0
          }
          val bf = ByteBuffer.allocate(features.length * 4)
          features foreach bf.putFloat
          BaseEncoding.base64().encode(bf.array())
        }
      case None => Future.None
    }
  }


}
