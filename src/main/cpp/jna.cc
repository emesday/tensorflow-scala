#include "tensorflow/core/public/session.h"
#include "tensorflow/core/platform/env.h"

using namespace tensorflow;

extern "C" {
  Session *tfCreateSession(const char *modelPath) { 
    Session* session;
    Status status = NewSession(SessionOptions(), &session);
    if (!status.ok()) {
      std::cout << status.ToString() << "\n";
      return NULL;
    }
    GraphDef graphDef;
    status = ReadBinaryProto(Env::Default(), modelPath, &graphDef);
    if (!status.ok()) {
      std::cout << status.ToString() << "\n";
      return NULL;
    }

    status = session->Create(graphDef);
    if (!status.ok()) {
      std::cout << status.ToString() << "\n";
      return NULL;
    }
    return session;
  }

  int tfRunString(Session *session, const char *inputLayer, const char *outputLayer, const char *data, int size, float *result) {
    string s(data, size);
    Tensor t(DT_STRING, TensorShape({}));
    t.scalar<string>()() = s;

    const Tensor& resized_tensor = t;

    std::vector<Tensor> outputs;
    Status run_status = session->Run({{inputLayer, resized_tensor}}, {outputLayer}, {}, &outputs);
    if (!run_status.ok()) {
      LOG(ERROR) << "Running model failed: " << run_status;
      return -1;
    }

    auto o = outputs[0].flat<float>();
    for (int i = 0; i < o.size(); i++) {
      result[i] = o(i);
    }
    return o.size();
  }

  int tfRunFloatArray(Session *session, const char *inputLayer, const char *outputLayer, const float *data, int size, float *result) {
    Tensor t(DT_FLOAT, TensorShape({1, size}));
    for (int i = 0; i < size ; i++)
      t.matrix<float>()(0, i) = data[i];

    const Tensor& resized_tensor = t;

    std::vector<Tensor> outputs;
    Status run_status = session->Run({{inputLayer, resized_tensor}}, {outputLayer}, {}, &outputs);
    if (!run_status.ok()) {
      LOG(ERROR) << "Running model failed: " << run_status;
      return -1;
    }

    auto o = outputs[0].flat<float>();
    for (int i = 0; i < o.size(); i++) {
      result[i] = o(i);
    }
    return o.size();
  }

//  int tfGetOutputSize(Session *session, const char *outputLayer) {
//    std::vector<Tensor> outputs;
//    Status run_status = session->Run({{}}, {outputLayer}, {}, &outputs);
//    if (!run_status.ok()) {
//      LOG(ERROR) << "Running model failed: " << run_status;
//      return -1;
//    }
//    return outputs[0].flat<float>().size();
//  }

  void tfCloseSession(Session *session) {
    session->Close();
  }
}

