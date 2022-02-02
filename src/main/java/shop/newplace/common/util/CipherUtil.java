package shop.newplace.common.util;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CipherUtil {
	
	private static class Cipher{
		private static String encrypt(String planeText, int subStrIndex) {
			StringBuilder sb = new StringBuilder();

			if("".equals(planeText)) {
				sb.append("");
			} else {
				String planeTextBefore = planeText.substring(0, subStrIndex);
				String cipherTextAfter = Optional.ofNullable(
												AESSecureUtil.encrypt(
														planeText.substring(subStrIndex, planeText.length())
														)
												).orElseThrow();
				sb.append(planeTextBefore);
				sb.append(cipherTextAfter);
			}
			
			
			log.info("encrpyt : " + sb.toString());
			
			return sb.toString();
		}
		
		private static String decrypt(String cipherText, int subStrIndex)  {
			
			StringBuilder sb = new StringBuilder();

			if("".equals(cipherText)) {
				sb.append("");
			} else {
				String cipherTextBefore = cipherText.substring(0, subStrIndex);
				String planeTextAfter = Optional.ofNullable(
											AESSecureUtil.decrypt(
												cipherText.substring(subStrIndex, cipherText.length())													
												)
											).orElseThrow();
				sb.append(cipherTextBefore);
				sb.append(planeTextAfter);
			}
			
			log.info("decrpyt : " + sb.toString());
			
			return sb.toString();
		}
		
	}
	
	public static class Email {
		private final static int SUB_STR_INDEX = 4;
		
		public static String encrypt(String planeText) {
			
			return Cipher.encrypt(planeText, SUB_STR_INDEX);
		}

		public static String decrypt(String cipherText) {

			return Cipher.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	public static class Phone {
		private final static int SUB_STR_INDEX = 2;
		
		public static String encrypt(String planeText) {
			
			return Cipher.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public static String decrypt(String cipherText) {
			
			return Cipher.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	public static class Name {
		private final static int SUB_STR_INDEX = 1;

		public static String encrypt(String planeText) {
			
			return Cipher.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public static String decrypt(String cipherText) {
			
			return Cipher.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	public static class BankId {
		private final static int SUB_STR_INDEX = 1;

		public static String encrypt(String planeText) {

			return Cipher.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public static String decrypt(String cipherText) {

			return Cipher.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	public static class AccountNumber {
		private final static int SUB_STR_INDEX = 3;

		public static String encrypt(String planeText)  {
			
			return Cipher.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public static String decrypt(String cipherText) {
			
			return Cipher.decrypt(cipherText, SUB_STR_INDEX);
		}
	}
}
