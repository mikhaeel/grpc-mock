package com.grpc.mock;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.lang.reflect.Method;

/**
 * Клиент для проверки вызова, чтобы не помещать файлы в classpath,
 * тоже грузим все из ресурсов рефлекшеном
 */
public class ClientApplication {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        ClassLoader classLoader = new ClassLoaderFactory()
                .newClassLoader("src/test/resources/worker-project/target/classes");
        Method newBlockingStubMethod = classLoader
                .loadClass("com.sbt.sbermock.HelloServiceGrpc")
                .getDeclaredMethod("newBlockingStub", Channel.class);
        Object stub = newBlockingStubMethod.invoke(null, channel);
        Class<?> stubClass = stub.getClass();

        Class<?> requestClass = classLoader.loadClass("com.sbt.sbermock.HelloRequest");
        Object newBuilder = requestClass.getDeclaredMethod("newBuilder").invoke(null);
        Object requestInstance = newBuilder.getClass().getDeclaredMethod("build").invoke(newBuilder);

        Method sayHelloMethod = stubClass.getDeclaredMethod("sayHello", requestClass);

        Object responseInstance = sayHelloMethod.invoke(stub, requestInstance);

        System.out.println("Rs: " + responseInstance);


        channel.shutdown();
    }
}
