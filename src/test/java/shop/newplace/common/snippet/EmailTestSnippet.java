package shop.newplace.common.snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class EmailTestSnippet {
	
	public RequestFieldsSnippet SuccessGetRequest() {
		return requestFields(
 				  fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("사용자 이메일")
				, fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("이메일 인증번호")
				, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
				 );
	}
	
	public ResponseFieldsSnippet SuccessGetResponse() {
		return responseFields(
				 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
			   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
			   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
			   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
			   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("이메일 인증 체크")
			   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
			);
	}
	
	public RequestFieldsSnippet SuccessPostRequest() {
    	return requestFields(
				  fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("사용자 이메일")
				, fieldWithPath("certificationNumber").type(JsonFieldType.NULL).description("이메일 인증번호")
				, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
				 );
    }
	public ResponseFieldsSnippet SuccessPostResponse() {
    	return responseFields(
				 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
			   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
			   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
			   , fieldWithPath("description").type(JsonFieldType.NULL).description("설명")
			   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("이메일 인증 체크")
			   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
		);
    }
    
   	public RequestFieldsSnippet WrongCertificationNumberGetRequest() {
		return requestFields(
 				  fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("사용자 이메일")
				, fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("이메일 인증번호")
				, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
				 );
	}
	
	public ResponseFieldsSnippet WrongCertificationNumberGetResponse() {
		return responseFields(
				 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
			   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
			   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
			   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
			   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("이메일 인증 체크")
			   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
			);
	}
   
   	public RequestFieldsSnippet NullLoginEmailGetRequest() {
		return requestFields(
	 				  fieldWithPath("loginEmail").type(JsonFieldType.NULL).description("사용자 이메일")
					, fieldWithPath("certificationNumber").type(JsonFieldType.NULL).description("이메일 인증번호")
					, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
				);
   	}
   	
   	public ResponseFieldsSnippet NullLoginEmailGetResponse() {
		return responseFields(
					 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
				   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
				   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
				   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
				   , fieldWithPath("data").type(JsonFieldType.NULL).description("이메일 인증 체크")
				   , subsectionWithPath("errors").type(JsonFieldType.ARRAY).description("에러")
				);
   	}
}
