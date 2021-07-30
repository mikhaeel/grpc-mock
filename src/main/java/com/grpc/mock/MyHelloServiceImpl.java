package com.grpc.mock;

import com.sbt.sbermock.HelloReply;
import com.sbt.sbermock.HelloRequest;
import com.sbt.sbermock.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

public class MyHelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        System.out.println("Start procedure");
        String greeting = new StringBuilder()
                .append("Hello, ")
                .append(request.getName())
                .append("!")
                .toString();

        HelloReply response = HelloReply.newBuilder()
                .setMessage(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println("Finish procedure");
    }
}
