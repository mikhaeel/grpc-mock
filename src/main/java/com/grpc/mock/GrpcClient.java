package com.grpc.mock;

import com.sbt.sbermock.HelloReply;
import com.sbt.sbermock.HelloRequest;
import com.sbt.sbermock.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub
                = HelloServiceGrpc.newBlockingStub(channel);

        HelloReply helloResponse = stub.sayHello(HelloRequest.newBuilder()
                .setName("gRPC")
                .build());

        System.out.println("Response is: " + helloResponse.getMessage());

        channel.shutdown();
    }
}
