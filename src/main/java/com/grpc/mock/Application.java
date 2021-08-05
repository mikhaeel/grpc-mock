package com.grpc.mock;

import com.google.common.collect.Lists;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.lang.reflect.InvocationHandler;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class Application {

    public static void main(String[] args) throws Exception {

        // Генерация Java файлов из proto файла
        InvocationRequest request = new DefaultInvocationRequest();

        request.setPomFile(new File( "src/test/resources/worker-project/pom.xml" ) );
        request.setGoals(Lists.newArrayList("clean", "install"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:/Apps/apache-maven-3.8.1"));
      //  invoker.setMavenHome(new File("/Users/mike/Apps/apache-maven-3.6.3"));
        InvocationResult result = invoker.execute(request);
        if (result.getExitCode() != 0) {
            throw new RuntimeException(result.getExecutionException());
        }

        ClassLoader classLoader = new ClassLoaderFactory()
                .newClassLoader("src/test/resources/worker-project/target/classes");

        Class<?> aClass = classLoader.loadClass("com.sbt.sbermock.HelloServiceGrpc");
        System.out.println("aClass = " + aClass);
        Class<?> implBase = classLoader.loadClass("com.sbt.sbermock.HelloServiceGrpc$HelloServiceImplBase");

        System.out.println("ImplBase: " + implBase);

        Class<?> serviceClass = new ByteBuddy().subclass(implBase)
                .defineField("mockHandler", InvocationHandler.class, Visibility.PUBLIC)
                .method(isDeclaredBy(implBase))
                .intercept(InvocationHandlerAdapter.toField("mockHandler"))
                .make()
                .load(classLoader)
                .getLoaded();
        Object serviceInstance = serviceClass.newInstance();
        serviceClass.getDeclaredField("mockHandler")
                .set(serviceInstance, new GrpcInvocationHandler());

        Server server = ServerBuilder
                .forPort(8080)
                .addService((BindableService) serviceInstance)
                .build();

        server.start();
        server.awaitTermination();
    }
}
