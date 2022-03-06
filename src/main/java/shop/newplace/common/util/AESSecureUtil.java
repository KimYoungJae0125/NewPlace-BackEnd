package shop.newplace.common.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESSecureUtil {
	
	public static String alg = "AES/CBC/PKCS5Padding";
	private final static String KEY = "01234567890123456789012345678901";	//256bit, 32byte 문자열
	private final static String IV = KEY.substring(0, 16);	//해당 로직은 사용하면 16byte 문자열로 이용

	public static String encrypt(String planeText) {
		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
			
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
			
			byte[] encrpyted = cipher.doFinal(planeText.getBytes("UTF-8"));
			
			return Base64.getEncoder().encodeToString(encrpyted);
		} catch (Exception e) {
			return null;
		} 
	}
	
	public static String decrypt(String cipherText) {
		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
			
			byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
			byte[] decrypted = cipher.doFinal(decodedBytes);
			
			return new String(decrypted, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}
	
}
