package org.mskim.tensorflow

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import org.mskim.tensorflow.controllers.TensorFlowServingController

object ServerMain extends Server

class Server extends HttpServer {

  override def configureHttp(router: HttpRouter) {
    router
      .filter[CommonFilters]
      .add[TensorFlowServingController]
  }

}
