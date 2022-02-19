package shop.newplace.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class JasyptEncryptTest {

	@Value("${jasypt.encryptor.password")
	private String encryptKey;
	
	@Test
	public void encryptDecryptTest() {
//		String plainText = "newPlace123";
//		String plainText = "MWP4RQLXVPCW";
		String[] plainText = {"smtp.naver.com", "465", "smtptester@naver.com"};
		
		StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
		jasypt.setPassword(encryptKey);
		jasypt.setAlgorithm("PBEWithMD5AndDES");
		
		for(String plain : plainText) {
			String encrpytedText = jasypt.encrypt(plain);
			System.out.println("enc : " + encrpytedText);
			String decryptedText = jasypt.decrypt(encrpytedText);
			System.out.println("dec : " + decryptedText);
		}
//		String encrpytedText = jasypt.encrypt(plainText);
//		System.out.println("enc : " + encrpytedText);
//		String decryptedText = jasypt.decrypt(encrpytedText);
//		System.out.println("dec : " + decryptedText);
		
	}
	
}
