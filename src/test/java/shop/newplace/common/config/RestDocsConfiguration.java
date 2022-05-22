package shop.newplace.common.config;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

@TestConfiguration
public class RestDocsConfiguration {
	 
	@Bean
	public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
	    return configurer -> configurer.operationPreprocessors()
	        .withRequestDefaults(prettyPrint())
	        .withResponseDefaults(prettyPrint());
	}
	
	public static OperationRequestPreprocessor getDocumentRequest() {
		return preprocessRequest(
					modifyUris()
								.scheme("https")
								.host("www.newplace.shop")
								.removePort()
				 , prettyPrint()
				);
	}
	
	public static OperationResponsePreprocessor getDocumentResponse() {
		return preprocessResponse(prettyPrint());
	}
	
}
