package com.grpc.mock;

import com.google.common.collect.Lists;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Application {

    public static void main(String[] args) throws Exception {

        // Генерация Java файлов из proto файла
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( "/Users/mike/projects/worker-project/pom.xml" ) );
        request.setGoals(Lists.newArrayList("clean", "install"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("/Users/mike/Apps/apache-maven-3.6.3"));
        invoker.execute(request);

        addPath("/Users/mike/projects/worker-project/target/classes");

        Class<?> aClass = Class.forName("com.sbt.sbermock.HelloServiceGrpc");
        System.out.println("aClass = " + aClass);

//        Server server = ServerBuilder
//                .forPort(8080)
//                .addService(new MyHelloServiceImpl())
//                .build();
//
//        server.start();
//        server.awaitTermination();
    }

    public static void addPath(String s) throws Exception {
        File f = new File(s);
        URL u = f.toURL();
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u});
    }
}
