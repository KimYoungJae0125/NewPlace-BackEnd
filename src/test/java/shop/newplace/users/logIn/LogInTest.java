package shop.newplace.users.logIn;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.newplace.common.config.RestDocsConfiguration.getDocumentRequest;
import static shop.newplace.common.config.RestDocsConfiguration.getDocumentResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.users.model.dto.UsersRequestDto;
import shop.newplace.users.repository.UsersRepository;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
@AutoConfigureRestDocs
class LogInTest {

	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
    private UsersRepository usersRepository;
    
	@Autowired
	ObjectMapper objectMapper;
	
	String loginEmail = "abcdefg@newPlace";
	String name = "테스터";
	String password = "abcdefg!@#1";
	String mainPhoneNumber = "01012345678";
	String bankId = "01";
	String accountNumber = "12345678";
	
	
    @BeforeEach
    public void setup() throws Exception {

    	UsersRequestDto.SignUp signUpForm = UsersRequestDto.SignUp.builder()
																  .loginEmail(loginEmail)
																  .password(password)
																  .passwordVerified(password)
																  .bankId(bankId)
																  .accountNumber(accountNumber)
																  .mainPhoneNumber(mainPhoneNumber)
																  .name(name)
																  .emailVerified(true)
																  .build();
    	mockMvc.perform(post("/users")
    						.contentType(MediaType.APPLICATION_JSON)
    						.content(objectMapper.writeValueAsString(signUpForm)))
    			.andExpect(status().isOk())
    			.andDo(print());
    }
	
	
    @DisplayName("정상 로그인 테스트")
    @Test
    void logInTest() throws Exception {

    	UsersRequestDto.LogIn signInForm = UsersRequestDto.LogIn.builder()
                            									.loginEmail(loginEmail)
                            									.password(password)
                            									.build();
    	
    	ResultActions resultActions = mockMvc.perform(post("/users/login")
					    			.contentType(MediaType.APPLICATION_JSON)
					    			.content(objectMapper.writeValueAsString(signInForm))
									);
//								.andExpect(status().isOk())
//								.andDo(print());

    	resultActions.andExpect(status().isOk())
//			    	 .andExpect(jsonPath("title").value("title"))
//			    	 .andExpect(jsonPath("body").value("body"))
//			    	 .andExpect(jsonPath("views").value(0))
			    	 .andDo(document("user/login"
			    			 		, getDocumentRequest()
			    			 		, getDocumentResponse()
			    			 		, requestFields(
					    					 fieldWithPath("loginEmail").type(JsonFieldType.STRING).description("로그인 사용 이메일")
					    				   , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
					    					 ),
					    			 responseFields(
					    					 fieldWithPath("transactionTime").type(JsonFieldType.STRING).description("트랜잭션이 일어난 시간")
					    				   , fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태 코드")
					    				   , fieldWithPath("responseMessage").type(JsonFieldType.STRING).description("반환 메시지")
					    				   , fieldWithPath("description").type(JsonFieldType.STRING).description("설명")
					    				   , subsectionWithPath("data").type(JsonFieldType.OBJECT).description("액세스토큰")
					    				   , fieldWithPath("errors").type(JsonFieldType.NULL).description("에러")
					    					 )
				    			 )
			    			);
    	
    }
    

//    @DisplayName("로그인 성공")
//    @Test
//    @Transactional
//    public void login_success() throws Exception {
//
//    	String loginEmail = "abcdefg@naver.com";
//    	String password = "abcdefg";
//    	
//    	SignUpForm signUpForm = createUsers(loginEmail, password);
//    	
//        mockMvc.perform(formLogin().user(signUpForm.getLoginEmail())
//        						   .password(passwordEncoder.encode(password)))
//        				.andExpect(authenticated())
//				        .andDo(print());
//    }
//
//    @DisplayName("로그인 실패")
//    @Test
//    @Transactional
//    public void login_fail() throws Exception {
//    	
//    	String loginEmail = "abcdefg@naver.com";
//    	String password = "abcdefg";
//    	
//    	SignUpForm signUpForm = createUsers(loginEmail, password);
//    	
//    	mockMvc.perform(formLogin().user(signUpForm.getLoginEmail())
//    							   .password("12345"))
//    					.andExpect(authenticated())
//    					.andDo(print());
//    }
//
//    private SignUpForm createUsers(String loginEmail, String password) {
//    	SignUpForm signUpForm = SignUpForm.builder()
//			    					   	  .name("테스터")
//			    					   	  .loginEmail(loginEmail)
//			    					      .password(password)
//			    					      .passwordVerified(password)
//			    					      .mainPhoneNumber("01012345678")
//			    					      .bankId("01")
//			    					      .accountNumber("1234567")
//			    					      .build();
////    	usersService.signUp(signUpForm);
//    	
//    	return signUpForm;
//    }
    


}