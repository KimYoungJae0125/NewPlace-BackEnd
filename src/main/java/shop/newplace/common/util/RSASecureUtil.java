package shop.newplace.common.util;

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
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSASecureUtil {
	
	/**
	 * 1024비트 RSA 키쌍 생성
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {
		SecureRandom secureRandom = new SecureRandom();
		
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(1024, secureRandom);
		
		KeyPair keyPair = gen.genKeyPair();
		
		return keyPair;
	}
	
	/**
	 * 공개키로 RSA 암호화를 수행
	 * @param plainText	: 암호화할 평문
	 * @param publicKey	: 공개키
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static String encryptRSA(String plainText, PublicKey publicKey) throws NoSuchPaddingException
																				, NoSuchAlgorithmException
																				, InvalidKeyException
																				, BadPaddingException
																				, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytePlain = cipher.doFinal(plainText.getBytes());
		String encrypted = Base64.getEncoder().encodeToString(bytePlain);

		return encrypted;
	}
	
	/**
	 * 비밀키로 RSA 복호화 수행
	 * @param encryted		: 암호화된 이진데이터를 base64 인코딩한 문자열
	 * @param privateKey	: 복호화를 위한 개인키(비밀키)
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptRSA(String encryted, PrivateKey privateKey)	throws NoSuchPaddingException
																				 , NoSuchAlgorithmException
																				 , InvalidKeyException
																				 , BadPaddingException
																				 , IllegalBlockSizeException
																				 , UnsupportedEncodingException {
		
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] byteEncrypted = Base64.getDecoder().decode(encryted.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytePlain = cipher.doFinal(byteEncrypted);
		String decrypted = new String(bytePlain, "utf-8");
		
		return decrypted;
	}
	
	/**
	 * 인코딩 된 개인(비밀)키 
	 * 문자열로부터 PrivateKey객체를 얻음
	 * @param keyString
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PrivateKey getPrivateKeyFromBase64String(final String keyString) throws NoSuchAlgorithmException
																						, InvalidKeySpecException {
		final String PRIVATE_KEY_STRING = keyString.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]+-{5}", "");
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY_STRING));
		
		return keyFactory.generatePrivate(keySpecPKCS8);
	}

	/**
	 * 인코딩 된 공용키
	 * 문자열로부터 PublicKey 객체를 얻음
	 * @param KEY_STRING
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKeyFromBase64String(final String KEY_STRING) throws NoSuchAlgorithmException
																					   , InvalidKeySpecException {
		final String PUBLIC_KEY_STRING = KEY_STRING.replaceAll("\\n", "").replaceAll("-{5}[ a-zA-Z]*-{5}", "");
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY_STRING));
		
		return keyFactory.generatePublic(keySpecX509);
	}
	
}
