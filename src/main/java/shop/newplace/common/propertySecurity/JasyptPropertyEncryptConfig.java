package shop.newplace.common.propertySecurity;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@Configuration
@EnableEncryptableProperties
public class JasyptPropertyEncryptConfig {

	@Value("${jasypt.encryptor.password")
	private String encrpytKey;
	
	final static String SECRET_KEY = "NPLACE_SECRETKEY";
	final static String ALGORITHM = "PBEWithMD5ANndDES";
	
	@Bean("jasyptStringEncryptor")
	public StringEncryptor stringEncrpytor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(encrpytKey);
		config.setAlgorithm(ALGORITHM);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
//		encryptor.setProvider(new BouncyCastleProvider());
//		encryptor.setPoolSize(2);
//		encryptor.setPassword(SECRET_KEY);
//		encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
		encryptor.setConfig(config);
		return encryptor;
	}

}
