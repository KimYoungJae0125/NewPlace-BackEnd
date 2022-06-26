package shop.newplace.integration.email;

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
import shop.newplace.common.snippet.EmailTestSnippet.Success;
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
    	EmailTestSnippet.Success succesSnippet = emailTestSnippet.new Success();
    	EmailRequestDto.EmailAuthentication emailDto = emailTestDto.createEmailAuthenticationForm();
    	
    	MvcResult mvcResult = mockMvc.perform(post("/email/authentication")
								    			.contentType(MediaType.APPLICATION_JSON)
								    			.content(objectMapper.writeValueAsString(emailDto))
								    			)
							    	 .andExpect(status().isOk())
							    	 .andDo(apiDocumentUtils.createAPIDocument("email/success/authentication/post", succesSnippet.PostRequest(), succesSnippet.PostResponse()))
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
		    	.andDo(apiDocumentUtils.createAPIDocument("email/success/authentication/get",  succesSnippet.GetRequest(), succesSnippet.GetResponse()))
		    	.andDo(print());
	}
    
    
    @DisplayName("틀린 인증번호 보내기")
    @Test
    void wrongCertificationNumberTest() throws Exception {
    	EmailTestSnippet.WrongCertificationNumber wrongCertificationNumberSnippet = emailTestSnippet.new WrongCertificationNumber();
    	EmailRequestDto.EmailAuthentication emailAuthenticationFormByWrongCertificationNumber = emailTestDto.createEmailAuthenticationFormByWrongCertificationNumber();
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailAuthenticationFormByWrongCertificationNumber))
				)
			.andExpect(status().isOk())
			.andDo(apiDocumentUtils.createAPIDocument("email/fail/wrong-certification-number/get", wrongCertificationNumberSnippet.GetRequest(), wrongCertificationNumberSnippet.GetResponse()))
			.andDo(print());
    }

    @DisplayName("이메일 데이터 없음")
    @Test
    void nullLoginEmailTest() throws Exception {
    	EmailTestSnippet.NullLoginEmail nullLoginEmailSnippet = emailTestSnippet.new NullLoginEmail();
    	EmailRequestDto.EmailAuthentication nullEmailAuthenticationForm = emailTestDto.createNullEmailAuthenticationForm();
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(nullEmailAuthenticationForm))
				)
			.andExpect(status().isBadRequest())
			.andDo(apiDocumentUtils.createAPIDocument("email/fail/null-login-email/get", nullLoginEmailSnippet.GetRequest(), nullLoginEmailSnippet.GetResponse()))
			.andDo(print());
    	
    }
    

    
}