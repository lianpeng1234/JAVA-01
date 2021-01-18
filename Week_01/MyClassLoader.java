package com.lp.week01;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        MyClassLoader myc = new MyClassLoader();
        Class<?> c = myc.loadClass("Hello.xlass");//字符串是要加载类的 package
        Object obj = c.newInstance();
        System.out.println(obj);
        System.out.println(obj.getClass().getClassLoader());
        System.out.println(obj.getClass().getClassLoader().getParent());
    }

    @Override
    protected Class<?> findClass(String name) {
        Class<?> c = null;
        byte[] data = new byte[0];
        try {
            data = getClassFileBytes(name);
            c = defineClass(name, data, 0, data.length - 1);//name参数是要加载类的 package
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private byte[] getClassFileBytes(String name) throws Exception {
        String path = "/Users/liangpeng/jiketime/JAVA-01/Week_01/";
        String classFile = path + name;
        FileInputStream fis = new FileInputStream(classFile);
        FileChannel fileC = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel outC = Channels.newChannel(baos);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            int i = fileC.read(buffer);
            if (i == 0 || i == -1) {
                break;
            }
            buffer.flip();
            outC.write(buffer);
            buffer.clear();
        }
        fis.close();
        return baos.toByteArray();
    }

}
