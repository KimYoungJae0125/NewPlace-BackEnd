package shop.newplace.users.email;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.mail.model.dto.EmailResponseDto;
import shop.newplace.common.utils.APIDocument;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
@AutoConfigureRestDocs
class EmailTest {

	@Autowired
    private MockMvc mockMvc;
    
	private APIDocument apidocument = new APIDocument();
    
	@Autowired
	ObjectMapper objectMapper;
	
	final String loginEmail = "emailTester@newPlace";
	
    @DisplayName("이메일 인증 성공")
    @Test
	void successEmailAuthenticationTest() throws Exception {
    	Success success = new Success();
		
    	EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder()
													  .loginEmail(loginEmail)
													  .build();
    	MvcResult mvcResult = mockMvc.perform(post("/email/authentication")
								    			.contentType(MediaType.APPLICATION_JSON)
								    			.content(objectMapper.writeValueAsString(emailDto))
								    			)
							    	 .andExpect(status().isOk())
							    	 .andDo(apidocument.createAPIDocument("email/success/authentication/post", success.PostRequest(), success.PostResponse()))
							    	 .andDo(print())
							    	 .andReturn();
    	Object responseData = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class).get("data");
    	EmailResponseDto.Info emailInfo = objectMapper.readValue(objectMapper.writeValueAsString(responseData), EmailResponseDto.Info.class) ;
    	emailDto.setCertificationNumber(emailInfo.getCertificationNumber());

    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
    			)
		    	.andExpect(status().isOk())
		    	.andDo(apidocument.createAPIDocument("email/success/authentication/get", success.GetRequest(), success.GetResponse()))
		    	.andDo(print());
	}
    
    
    @DisplayName("틀린 인증번호 보내기")
    @Test
    void wrongCertificationNumberTest() throws Exception {
    	WrongCertificationNumber wrongCertificationNumber = new WrongCertificationNumber();
    	
    	EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder()
    									  .loginEmail(loginEmail)
    									  .certificationNumber(certificationNumber())
    									  .build();
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
				)
			.andExpect(status().isOk())
			.andDo(apidocument.createAPIDocument("email/fail/wrong-certification-number/get", wrongCertificationNumber.GetRequest(), wrongCertificationNumber.GetResponse()))
			.andDo(print());
    }

    @DisplayName("이메일 데이터 없음")
    @Test
    void nullLoginEmailTest() throws Exception {
    	NullLoginEmail nullLoginEmail = new NullLoginEmail();
    	
    	EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder().loginEmail(null).build();
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
				)
			.andExpect(status().isBadRequest())
			.andDo(apidocument.createAPIDocument("email/fail/null-login-email/get", nullLoginEmail.GetRequest(), nullLoginEmail.GetResponse()))
			.andDo(print());
    	
    }
    
    private String certificationNumber() {
		StringBuffer certificationNumber = new StringBuffer();
		IntStream.range(0, 6).forEach(i -> certificationNumber.append(new Random().ints(0, 9).findFirst().getAsInt()));
		return certificationNumber.toString();
    }
    
    private class Success {
    	private RequestFieldsSnippet GetRequest() {
    		return requestFields(
	 				  fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("사용자 이메일")
					, fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("이메일 인증번호")
					, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
					 );
    	}
    	
    	private ResponseFieldsSnippet GetResponse() {
    		return responseFields(
					 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
  				   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
  				   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
  				   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
  				   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("이메일 인증 체크")
  				   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
  				);
    	}
    	
        private RequestFieldsSnippet PostRequest() {
        	return requestFields(
    				  fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("사용자 이메일")
    				, fieldWithPath("certificationNumber").type(JsonFieldType.NULL).description("이메일 인증번호")
    				, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
    				 );
        }
        private ResponseFieldsSnippet PostResponse() {
        	return responseFields(
    				 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
    			   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
    			   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
    			   , fieldWithPath("description").type(JsonFieldType.NULL).description("설명")
    			   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("이메일 인증 체크")
    			   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
    		);
        }
    }
    
   private class WrongCertificationNumber{
	   	private RequestFieldsSnippet GetRequest() {
    		return requestFields(
	 				  fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("사용자 이메일")
					, fieldWithPath("certificationNumber").type(JsonFieldType.STRING).description("이메일 인증번호")
					, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
					 );
    	}
    	
    	private ResponseFieldsSnippet GetResponse() {
    		return responseFields(
					 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
  				   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
  				   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
  				   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
  				   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("이메일 인증 체크")
  				   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
  				);
    	}
   }
   
   private class NullLoginEmail{
	   	private RequestFieldsSnippet GetRequest() {
   		return requestFields(
	 				  fieldWithPath("loginEmail").type(JsonFieldType.NULL).description("사용자 이메일")
					, fieldWithPath("certificationNumber").type(JsonFieldType.NULL).description("이메일 인증번호")
					, fieldWithPath("expirationTime").type(JsonFieldType.NULL).description("만료 시간")
					 );
   	}
   	
   	private ResponseFieldsSnippet GetResponse() {
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
    
}