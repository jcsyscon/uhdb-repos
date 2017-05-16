/**
 *
 */
package com.realsnake.sample.util.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * @author 전강욱(realsnake1975@gmail.com), 2016. 6. 27.
 * @description
 */
public class RsaCipherUtils {

    public static final String ALGO_RSA = "RSA";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final int RSA_KEY_LENGTH = 1024; // 512, 1024, 2048
    public static final String PRIKEY_SAVING_EXT = "pse";

    /**
     * RSA 공개키와 개인키를 생성한다.
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static RsaKey generateRsaKey() throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {
        return generateRsaKey(RSA_KEY_LENGTH, null);
    }

    /**
     * RSA 공개키와 개인키를 생성한다.
     *
     * @param keyLength
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Deprecated
    public static RsaKey generateRsaKey(int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {
        return generateRsaKey(keyLength, null);
    }

    /**
     * RSA 공개키와 개인키를 생성한 후 개인키를 파일로 저장한다.
     *
     * @param uuid
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Deprecated
    public static RsaKey generateRsaKey(String uuid) throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {
        return generateRsaKey(RSA_KEY_LENGTH, uuid);
    }

    /**
     * RSA 공개키와 개인키를 생성한 후 개인키를 파일로 저장한다.
     *
     * @param keyLength
     * @param uuid
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static RsaKey generateRsaKey(int keyLength, String uuid) throws NoSuchAlgorithmException, InvalidKeySpecException, FileNotFoundException, IOException {
        SecureRandom sr = new SecureRandom();
        sr.nextInt();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGO_RSA);
        kpg.initialize(keyLength, sr);

        KeyPair kp = kpg.genKeyPair();
        // RSA에서 PublicKey의 byte[]은 modulus와 exponent의 조합으로 이루어진 ASN.1 포맷(publicKey.getEncoded())
        PublicKey publicKey = kp.getPublic(); // 공개키(사용자에게 발급)
        PrivateKey privateKey = kp.getPrivate(); // 개인키(서버에 저장?)

        if (uuid != null) {
            saveRsaPrivateKey(uuid, privateKey); // 개인키를 파일로 저장
        }

        return new RsaKey(keyLength, publicKey.getEncoded(), privateKey.getEncoded());
    }

    /**
     * RSA 개인키를 파일로 저장한다.
     *
     * @param uuid
     * @param privateKey
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Deprecated
    private static void saveRsaPrivateKey(String uuid, PrivateKey privateKey) throws FileNotFoundException, IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(System.getProperty("java.io.tmpdir") + File.separator + uuid + "." + PRIKEY_SAVING_EXT));
        oos.writeObject(privateKey);
        oos.close();
    }

    /**
     * 파일로 저장한 RSA 개인키를 읽어 반환한다. 별도 개인키 파일 삭제 스케줄링 기능 필요
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Deprecated
    private static PrivateKey readRsaPrivateKey(String uuid) throws Exception {
        long cdt = new Date().getTime();

        File f = null;
        PrivateKey privateKey = null;

        try {
            f = new File(System.getProperty("java.io.tmpdir") + File.separator + uuid + "." + PRIKEY_SAVING_EXT);
            long fcdt = f.lastModified();

            if ((cdt - fcdt) > 180000) { // 생성된지 180초가 지난 개인키는 예외 처리!
                throw new Exception("<<PrivateKey is timeover.>>");
            }

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            privateKey = (PrivateKey) ois.readObject();
            ois.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (f != null && f.exists()) {
                f.delete();
            }
        }

        return privateKey;
    }

    /**
     * Base64로 인코딩된 RSA로 암호화된 문자열을 복호화한다.
     *
     * @param encodedPrivateKey
     * @param encStr Base64로 인코딩된 암호화된 문자열
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] encodedPrivateKey, String encStr) throws Exception {
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGO_RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        Cipher cipher = Cipher.getInstance(ALGO_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encStr.getBytes()));
        String decStr = new String(decryptedBytes, CHARSET_UTF8);

        return decStr;
    }

    /**
     * 문자열을 RSA로 암호화한 후 BASE64 인코딩된 문자열을 반환한다.
     *
     * @param encodedPublicKey
     * @param plainText
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(byte[] encodedPublicKey, String plainText)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGO_RSA);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Cipher cipher = Cipher.getInstance(ALGO_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encodedPlainText = new String(plainText).getBytes(CHARSET_UTF8);
        byte[] encryptedBytes = cipher.doFinal(encodedPlainText);

        return Base64.encodeBase64String(encryptedBytes);
    }

    /* @formatter:off */
//    public static void main(String[] args) throws Exception {
//        String plainText = "안녕하세요? 전강욱입니다.";
//
//        RsaKey rsaKey = RsaCipherUtils.generateRsaKey();
//        System.out.println(rsaKey.toString());
//
//        String encStr = RsaCipherUtils.encrypt(rsaKey.getEecodedPublicKey(), plainText);
//        System.out.println("* RSA 암호화로 암호화된 평문: " + encStr);
//
//        String decStr = RsaCipherUtils.decrypt(rsaKey.getEecodedPrivateKey(), encStr);
//        System.out.println("* RSA 복호화로 복호화된 암호문: " + decStr);
//    }
    /* @formatter:on */

}
