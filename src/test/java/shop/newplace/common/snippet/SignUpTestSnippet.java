package shop.newplace.common.snippet;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class SignUpTestSnippet {
	
    public RequestFieldsSnippet SuccessPostRequest() {
    	return requestFields(
				 fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("로그인 사용 이메일")
				   , fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
				   , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				   , fieldWithPath("passwordVerified").type(JsonFieldType.STRING).description("비밀번호 확인")
				   , fieldWithPath("mainPhoneNumber").type(JsonFieldType.STRING).description("사용자 전화번호")
				   , fieldWithPath("bankId").type(JsonFieldType.STRING).description("은행코드")
				   , fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌번호")
				   , fieldWithPath("emailVerified").type(JsonFieldType.BOOLEAN).description("이메일 인증 확인")
				   , fieldWithPath("profilesSignUp").type(JsonFieldType.NULL).description("프로필")
    	);
    }
    public ResponseFieldsSnippet SuccessPostResponse() {
    	return responseFields(
				 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
				   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
				   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
				   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
				   , fieldWithPath("data").type(JsonFieldType.NULL).description("프로필")
				   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러 메시지")
    	);
    }
}
