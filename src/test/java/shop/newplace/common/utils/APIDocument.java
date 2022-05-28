package shop.newplace.common.utils;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static shop.newplace.common.config.RestDocsConfiguration.getDocumentRequest;
import static shop.newplace.common.config.RestDocsConfiguration.getDocumentResponse;

import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.ResultHandler;

public class APIDocument {
	
	public ResultHandler createAPIDocument(String documentPath, RequestFieldsSnippet requestFieldsSnippet, ResponseFieldsSnippet responseFieldsSnippet) {
		return document(documentPath
		 		, getDocumentRequest()
		 		, getDocumentResponse()
		 		, requestFieldsSnippet
    			, responseFieldsSnippet
			 );
	}

}
