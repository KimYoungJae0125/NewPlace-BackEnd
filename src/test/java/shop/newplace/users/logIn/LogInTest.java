package shop.newplace.users.logIn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.common.model.dto.UsersTestDto;
import shop.newplace.common.snippet.LogInTestSnippet;
import shop.newplace.common.utils.APIDocumentUtils;
import shop.newplace.users.model.dto.UsersRequestDto;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
@AutoConfigureRestDocs
class LogInTest {

	@Autowired
    private MockMvc mockMvc;
    
	private APIDocumentUtils apiDocumentUtils = new APIDocumentUtils();

	private UsersTestDto usersTestDto = new UsersTestDto();
	
	private LogInTestSnippet logInTestSnippet = new LogInTestSnippet();
	
	@Autowired
	ObjectMapper objectMapper;
	
    @BeforeEach
    public void setup() throws Exception {

    	UsersRequestDto.SignUp signUpForm = usersTestDto.createSignUpForm();
    	
    	mockMvc.perform(post("/users")
    						.contentType(MediaType.APPLICATION_JSON)
    						.content(objectMapper.writeValueAsString(signUpForm)))
    			.andExpect(status().isOk())
    			.andDo(print());
    }
	
	
    @DisplayName("정상 로그인 테스트")
    @Test
    void successLogInTest() throws Exception {
    	
    	UsersRequestDto.LogIn logInForm = usersTestDto.createLogInForm();
    	
    	mockMvc.perform(post("/users/login")
					    			.contentType(MediaType.APPLICATION_JSON)
					    			.content(objectMapper.writeValueAsString(logInForm))
									)
    								.andExpect(status().isOk())
    								.andDo(apiDocumentUtils.createAPIDocument("user/login", logInTestSnippet.SuccessPostRequest(), logInTestSnippet.SuccessPostResponse()));
//								.andExpect(status().isOk())
//								.andDo(print());

    	
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