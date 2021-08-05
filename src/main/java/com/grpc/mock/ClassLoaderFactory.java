package com.grpc.mock;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderFactory {

    public ClassLoader newClassLoader(String path) throws Exception {
        File f = new File(path);
        URL u = f.toURI().toURL();
        // строка ниже не работает на java 11, поэтому создаем через new
//        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {u}, ClassLoader.getSystemClassLoader());
        Class urlClass = URLClassLoader.class;
        Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(urlClassLoader, new Object[]{u});

        return urlClassLoader;
    }
}
