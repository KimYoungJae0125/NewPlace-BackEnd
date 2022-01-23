package shop.newplace.common.util;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CipherUtil {
	
	private final AESSecureUtil aesSecureUtil;
	
	@Component
	private class Cipher{
		private String encrypt(String planeText, int subStrIndex) throws Exception {
			StringBuilder sb = new StringBuilder();

			if("".equals(planeText)) {
				sb.append("");
			} else {
				String planeTextBefore = planeText.substring(0, subStrIndex);
				String cipherTextAfter = aesSecureUtil.encrypt(
						planeText.substring(subStrIndex, planeText.length())
						);
				sb.append(planeTextBefore);
				sb.append(cipherTextAfter);
			}
			
			
			log.info("encrpyt : " + sb.toString());
			
			return sb.toString();
		}
		
		private String decrypt(String cipherText, int subStrIndex) throws Exception {
			
			StringBuilder sb = new StringBuilder();

			if("".equals(cipherText)) {
				sb.append("");
			} else {
				String cipherTextBefore = cipherText.substring(0, subStrIndex);
				String planeTextAfter = aesSecureUtil.decrypt(
						cipherText.substring(subStrIndex, cipherText.length())													
						);
				sb.append(cipherTextBefore);
				sb.append(planeTextAfter);
			}
			
			log.info("decrpyt : " + sb.toString());
			
			return sb.toString();
		}
		
	}
	
	@Component
	@RequiredArgsConstructor
	public class Email {
		private final Cipher cipherUtil;
		private final int SUB_STR_INDEX = 4;
		
		public String encrypt(String planeText) throws Exception {
			
			return cipherUtil.encrypt(planeText, SUB_STR_INDEX);
		}

		public String decrypt(String cipherText) throws Exception {

			return cipherUtil.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	@Component
	@RequiredArgsConstructor
	public class Phone {
		private final Cipher cipherUtil;
		private final int SUB_STR_INDEX = 2;
		
		public String encrypt(String planeText) throws Exception  {
			
			return cipherUtil.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public String decrypt(String cipherText) throws Exception  {
			
			return cipherUtil.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	@Component
	@RequiredArgsConstructor
	public class Name {
		private final Cipher cipherUtil;
		private final int SUB_STR_INDEX = 1;

		public String encrypt(String planeText) throws Exception  {
			
			return cipherUtil.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public String decrypt(String cipherText) throws Exception  {
			
			return cipherUtil.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	@Component
	@RequiredArgsConstructor
	public class BankId {
		private final Cipher cipherUtil;
		private final int SUB_STR_INDEX = 1;

		public String encrypt(String planeText) throws Exception  {

			return cipherUtil.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public String decrypt(String cipherText) throws Exception  {

			return cipherUtil.decrypt(cipherText, SUB_STR_INDEX);
		}
	}

	@Component
	@RequiredArgsConstructor
	public class AccountNumber {
		private final Cipher cipherUtil;
		private final int SUB_STR_INDEX = 3;

		public String encrypt(String planeText) throws Exception  {
			
			return cipherUtil.encrypt(planeText, SUB_STR_INDEX);
		}
		
		public String decrypt(String cipherText) throws Exception  {
			
			return cipherUtil.decrypt(cipherText, SUB_STR_INDEX);
		}
	}
}
