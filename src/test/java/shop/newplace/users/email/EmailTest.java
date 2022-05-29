package shop.newplace.users.email;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.mail.model.dto.EmailResponseDto;
import shop.newplace.common.model.dto.EmailTestDto;
import shop.newplace.common.snippet.EmailTestSnippet;
import shop.newplace.common.utils.APIDocumentUtils;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
@AutoConfigureRestDocs
class EmailTest {

	@Autowired
    private MockMvc mockMvc;
    
	private APIDocumentUtils apiDocumentUtils = new APIDocumentUtils();
    
	private EmailTestDto emailTestDto = new EmailTestDto();
	
	private EmailTestSnippet emailTestSnippet = new EmailTestSnippet();
	
	@Autowired
	ObjectMapper objectMapper;
	
    @DisplayName("이메일 인증 성공")
    @Test
	void successEmailAuthenticationTest() throws Exception {
		
    	EmailRequestDto.EmailAuthentication emailDto = emailTestDto.createEmailAuthenticationForm();
    	
    	MvcResult mvcResult = mockMvc.perform(post("/email/authentication")
								    			.contentType(MediaType.APPLICATION_JSON)
								    			.content(objectMapper.writeValueAsString(emailDto))
								    			)
							    	 .andExpect(status().isOk())
							    	 .andDo(apiDocumentUtils.createAPIDocument("email/success/authentication/post", emailTestSnippet.SuccessPostRequest(), emailTestSnippet.SuccessPostResponse()))
							    	 .andDo(print())
							    	 .andReturn();
    	Object responseData = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class).get("data");
    	EmailResponseDto.Info responseEmailInfo = objectMapper.readValue(objectMapper.writeValueAsString(responseData), EmailResponseDto.Info.class) ;
    	emailDto.setCertificationNumber(responseEmailInfo.getCertificationNumber());

    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
    			)
		    	.andExpect(status().isOk())
		    	.andDo(apiDocumentUtils.createAPIDocument("email/success/authentication/get", emailTestSnippet.SuccessGetRequest(), emailTestSnippet.SuccessGetResponse()))
		    	.andDo(print());
	}
    
    
    @DisplayName("틀린 인증번호 보내기")
    @Test
    void wrongCertificationNumberTest() throws Exception {
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailTestDto.createEmailAuthenticationFormByWrongCertificationNumber()))
				)
			.andExpect(status().isOk())
			.andDo(apiDocumentUtils.createAPIDocument("email/fail/wrong-certification-number/get", emailTestSnippet.WrongCertificationNumberGetRequest(), emailTestSnippet.WrongCertificationNumberGetResponse()))
			.andDo(print());
    }

    @DisplayName("이메일 데이터 없음")
    @Test
    void nullLoginEmailTest() throws Exception {
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailTestDto.createNullEmailAuthenticationForm()))
				)
			.andExpect(status().isBadRequest())
			.andDo(apiDocumentUtils.createAPIDocument("email/fail/null-login-email/get", emailTestSnippet.NullLoginEmailGetRequest(), emailTestSnippet.NullLoginEmailGetResponse()))
			.andDo(print());
    	
    }
    

    
}