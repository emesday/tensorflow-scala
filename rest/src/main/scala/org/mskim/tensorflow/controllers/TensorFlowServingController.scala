package org.mskim.tensorflow.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future

class TensorFlowServingController extends Controller {

  post("/v1/image/label") { request: Request =>
    Future.value("ok")
  }

}
