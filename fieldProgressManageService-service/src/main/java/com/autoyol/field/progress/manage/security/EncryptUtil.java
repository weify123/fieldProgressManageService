package com.autoyol.field.progress.manage.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;


/**
 * @comments
 * @author zg
 * @date 2011-10-25
 */
public class EncryptUtil {

	private static final String  ALGORITHM = "HmacSHA224";
	private static final String  ENCODEING = "UTF-8";
	private static final String ENCRYPT_KEY = "nvkzlY32Y3lJRweEv+9SmEyFLh64MflotJUQAQ==";
	private static final byte[] key = Base64.decode(ENCRYPT_KEY);
	private static final SecretKey secretKey = new SecretKeySpec(key,ALGORITHM);
	private static Mac mac;

	static{
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * ���ַ���м���
	 * @param text ����
	 * @return ����
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String encode(String text) throws Exception {
		mac = (mac == null) ? Mac.getInstance(secretKey.getAlgorithm()) : mac;
		mac.init(secretKey);
		byte[] codedText = mac.doFinal(text.getBytes(ENCODEING));
		return new String(Hex.encode(codedText),ENCODEING);
	}

    /**
     * SHA-1加密
     * @param info
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String shaDigest(String info) throws NoSuchAlgorithmException {
        //java.security.MessageDigest alg=java.security.MessageDigest.getInstance("MD5");
        MessageDigest alga= MessageDigest.getInstance("SHA-1");
        alga.update(info.getBytes());
        byte[] digesta=alga.digest();
        return byte2hex(digesta);
    }

    /**
     * 二行制转字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b){
        String hs="";
        String stmp="";
        for (int n = 0;n < b.length; n++) {
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs=hs+stmp;
            }
            if (n<b.length-1){
                hs=hs+":";
            }
        }
        return hs.toUpperCase();
    }


    public static void main(String[] args) throws Exception{
        System.out.print(shaDigest("31231"));
        System.out.println(encode("123456"));
    }
}
