package com.grpc.mock;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GrpcInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args != null && args.length == 2 && args[0] instanceof GeneratedMessageV3
                && args[1] instanceof StreamObserver) {
            GeneratedMessageV3 rq = (GeneratedMessageV3) args[0];
            System.out.println("--> Intercepted: rq " + rq.getClass() + " descriptor: " +
                    rq.getDescriptorForType());
            StreamObserver observer = (StreamObserver) args[1];

            Object response = null; // TODO: 06.08.2021 тут вызываем groovy, кастим и тд
            observer.onNext(response);
            observer.onCompleted();
        }

        return null;
    }
}
