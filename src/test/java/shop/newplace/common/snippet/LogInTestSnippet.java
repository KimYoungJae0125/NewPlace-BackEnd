package shop.newplace.common.snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class LogInTestSnippet {
	
	public RequestFieldsSnippet SuccessPostRequest() {
    	return requestFields(
				 fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("로그인 사용 이메일")
				   , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
		);
    }
	public ResponseFieldsSnippet SuccessPostResponse() {
    	return responseFields(
				 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
				   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
				   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
				   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
				   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("액세스토큰")
				   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
		);
    }

}
