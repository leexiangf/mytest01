package com.lxf.gateway.util;

import org.apache.commons.io.FileUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @ClassName : SignUtils
 * @Description TODO
 * @Date 2023/9/10 14:42
 * @Created lxf
 */
public class SignUtils {

    private static final String ALGORITHM ="RSA";

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private static String publicKeyStr  ;
    private static String privateKeyStr  ;
    private static String publicKeyPath  ;
    private static String privateKeyPath  ;

    static {
        ClassLoader classLoader = SignUtils.class.getClassLoader();
        publicKeyPath = classLoader.getResource("rsa.pub").getPath();
        privateKeyPath = classLoader.getResource("rsa.pri").getPath();
    }

    private static void writeKey2File() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        String pubKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        PrivateKey privateKey = keyPair.getPrivate();
        String priKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        FileUtils.writeStringToFile(new File(publicKeyPath),pubKey);
        FileUtils.writeStringToFile(new File(privateKeyPath),priKey);
    }

    private static PublicKey getPublicKey() throws Exception {
        String publicKeyBase64 = FileUtils.readFileToString(new File(publicKeyPath), UTF8);
        byte[] decode = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }
    private static PrivateKey getPrivateKey() throws Exception {
        String publicKeyBase64 = FileUtils.readFileToString(new File(privateKeyPath), UTF8);
        byte[] decode = Base64.getDecoder().decode(publicKeyBase64);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    /**
     * 加密
     * @param original 原始内容
     * @param key 公钥、私钥
     * @return
     */
    public static String encrypt(String original, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] bytes = doCode(cipher,original.getBytes(UTF8),MAX_ENCRYPT_BLOCK);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密
     * @param encryptStr 加密后的内容
     * @param key 公钥、私钥
     * @return
     */
    public static String decrypt(String encryptStr, Key key) throws Exception {
        byte[] decode = Base64.getDecoder().decode(encryptStr);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] bytes = doCode(cipher,decode,MAX_DECRYPT_BLOCK);
        return new String(bytes,UTF8);
    }

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static byte[] doCode(Cipher cipher, byte[] bytes,int maxBlock) throws Exception {
        int length = bytes.length;
        int offset = 0;
        byte[] cache ;
        byte[] codeBytes;
        int i = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((length - offset) > 0){
                if((length - offset) > maxBlock){
                    cache =  cipher.doFinal(bytes,offset,maxBlock);
                }else {
                    cache =  cipher.doFinal(bytes,offset,length-offset);
                }
                bos.write(cache,0,cache.length);
                i++;
                offset = i*maxBlock;
            }
            codeBytes = bos.toByteArray();
        }finally {
            bos.close();
        }
        return codeBytes;
    }
}
