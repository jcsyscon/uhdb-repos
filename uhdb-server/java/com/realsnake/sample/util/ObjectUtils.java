/**
 * Copyright (c) 2017 realsnake1975@gmail.com
 *
 * 2017. 4. 11.
 */
package com.realsnake.sample.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * <pre>
 * Class Name : ObjectUtil.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2017. 4. 11.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2017. 4. 11.
 * @version 1.0
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    /* @formatter:off */
    /**
    private T t;

    public ObjectUtil() {
        //
    }

    public void add(T obj) {
        this.t = obj;
    }

    public T get() {
        return t;
    }
    */
    /* @formatter:on */

    /**
     * 객체 읽기
     *
     * @param base64EncodedObj
     * @param clazz
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T readObjectFromBase64EncodedString(String base64EncodedObj, Class<T> clazz) throws IOException, ClassNotFoundException {
        byte[] base64DecodedObj = Base64.decodeBase64(base64EncodedObj);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(base64DecodedObj));
        // return type.cast(ois.readObject());
        return (T) ois.readObject();
    }

    /**
     * 객체 읽기
     *
     * @param base64EncodedObj
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T readObjectFromBase64EncodedString(String base64EncodedObj) throws IOException, ClassNotFoundException {
        return ObjectUtils.readObjectFromBase64EncodedString(base64EncodedObj, null);
    }

    /**
     * 객체 저장
     *
     * @param object
     * @throws IOException
     */
    public static String saveObjectToBase64EncodedString(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baos);
        oout.writeObject(object);
        oout.close();
        byte[] buf = baos.toByteArray();
        String base64EncodedObj = Base64.encodeBase64String(buf);
        // System.out.printf("<<BASE64 인코딩된 객체>> %s", base64EncodedObj);
        return base64EncodedObj;
    }

    /* @formatter:off */
    /**
    public static void main(String[] args) throws Exception {
        SampleVo sample1 = new SampleVo();
        sample1.setName("전강욱");
        sample1.setRegDate(new Date());

        String base64EncodedObj = ObjectUtils.saveObjectToBase64EncodedString(sample1);

        SampleVo sample2 = ObjectUtils.readObjectFromBase64EncodedString(base64EncodedObj);
        System.out.println();
        System.out.println(sample2.toString());
    }
    */
    /* @formatter:on */

}
