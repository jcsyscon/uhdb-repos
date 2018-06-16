/**
 *
 */
package com.realsnake.sample.util.crypto;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;

/**
 * @author 전강욱(realsnake1975@gmail.com), 2016. 6. 27.
 * @description
 */
public class RsaKey implements Serializable {

    /** SID */
    private static final long serialVersionUID = 662493106725455785L;

    /**
     * 기본 생성자
     */
    public RsaKey() {
        //
    }

    /**
     * @param keyLength
     * @param publicKey
     * @param privateKey
     */
    public RsaKey(int keyLength, byte[] encodedPublicKey, byte[] encodedPrivateKey) {
        this();
        this.keyLength = keyLength;
        this.publicKey = Base64.encodeBase64String(encodedPublicKey);
        this.privateKey = Base64.encodeBase64String(encodedPrivateKey);
    }

    /**
     * RSA 키 길이
     */
    private int keyLength;
    /**
     * 공개키
     */
    private String publicKey;
    /**
     * 개인키
     */
    private String privateKey;


    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getEecodedPublicKey() {
        return Base64.decodeBase64(publicKey);
    }

    public byte[] getEecodedPrivateKey() {
        return Base64.decodeBase64(privateKey);
    }

    @Override
    public String toString() {
        return "RsaKey [keyLength=" + keyLength + "\n, publicKey=" + publicKey + "\n, privateKey=" + privateKey + "]";
    }

}
