package shop.newplace.integration.users;

//import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.common.constant.Role;
import shop.newplace.common.model.dto.SignUpTestDto;
import shop.newplace.common.snippet.SignUpTestSnippet;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.common.utils.APIDocumentUtils;
import shop.newplace.users.model.dto.UsersRequestDto;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.UsersRepository;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RequiredArgsConstructor	//test는 Autowired로
@AutoConfigureRestDocs
class SignUpTest {

	@Autowired
    MockMvc mockMvc;
    
	@Autowired
    UsersRepository usersRepository;
	
	@Autowired
	ObjectMapper objectMapper;

	private APIDocumentUtils apiDocumentUtils = new APIDocumentUtils();
	
	private SignUpTestDto signUpTestDto = new SignUpTestDto();
	
	private SignUpTestSnippet signUpTestSnippet = new SignUpTestSnippet();

    @BeforeEach
    void setup() {
    	this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//    	this.mockMvc = MockMvcBuilders
//    				   .webAppContextSetup(this.context)
////    				   .addFilter(new CharacterEncodingFilter("UTF-8", true))
//    				   .alwaysDo(print())
//    				   .build();
    }
    
    @AfterEach
    void unSet() {
    	usersRepository.deleteAll();
    }

    @DisplayName("정상 회원가입 테스트")
    @Test
//    @Disabled
    void signupNormalTest() throws Exception {
    	SignUpTestSnippet.Success successSnippet = signUpTestSnippet.new Success();
    	UsersRequestDto.SignUp signUpForm = signUpTestDto.createSignUpForm();
    	
    	ResultActions result =  mockMvc.perform(post("/users")
			    			.contentType(MediaType.APPLICATION_JSON)
			    			.content(objectMapper.writeValueAsString(signUpForm))
    						)
    					.andExpect(status().isOk())
    					.andDo(apiDocumentUtils.createAPIDocument("user/signup", successSnippet.PostRequest(), successSnippet.PostResponse()));
//    					.andDo(print());
    	
    	List<Users> usersList = usersRepository.findAll();
    	
    	Users users = usersList.get(0);
    	assertThat(CipherUtil.Email.decrypt(users.getLoginEmail()), is(signUpForm.getLoginEmail()));
    	assertThat(CipherUtil.Name.decrypt(users.getName()), is(signUpForm.getName()));
    }
    
    @DisplayName("JSON아닌 형식으로 파라미터 보내기")
    @Test
//    @Disabled
    void signupFailureTest() throws Exception {
    	UsersRequestDto.SignUp signUpForm = signUpTestDto.createSignUpForm();

    	mockMvc.perform(post("/users")
    						.param("name", signUpForm.getName())
    						.param("loginEmail", signUpForm.getLoginEmail())
    						.param("password", signUpForm.getPassword())
    						.param("mainPhoneNumber ", signUpForm.getMainPhoneNumber())
    						.param("bankId", signUpForm.getBankId())
    						.param("accountNumber", signUpForm.getAccountNumber())
    						.param("authId", String.valueOf(Role.USER.getValue()))
    			)
    	.andExpect(status().isBadRequest());
    }
    
    @DisplayName("회원가입 Valid EmailFailure 테스트")
    @Test
//    @Disabled
    void signupValidEmailFailureTest() throws Exception {
    	UsersRequestDto.SignUp signUpFormByWrongLoginEmail = signUpTestDto.createSignUpFormByWrongLoginEmail();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpFormByWrongLoginEmail))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 Password ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordFailureTest() throws Exception {
    	UsersRequestDto.SignUp signUpFormByWrongPassword = signUpTestDto.createSignUpFormByWrongPassword();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpFormByWrongPassword))
    			)
    	.andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 PasswordVerified ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordVerifiedFailureTest() throws Exception {
    	UsersRequestDto.SignUp signUpFormByWrongPasswordVerified = signUpTestDto.createSignUpFormByWrongPasswordVerified();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpFormByWrongPasswordVerified))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 Phone ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPhoneFailureTest() throws Exception {
    	UsersRequestDto.SignUp signUpFormByWrongMainPhoneNumber = signUpTestDto.createSignUpFormByWrongMainPhoneNumber();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpFormByWrongMainPhoneNumber))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 NULL ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidNullFailureTest() throws Exception {
    	UsersRequestDto.SignUp nullSignUpForm = signUpTestDto.createNullSignUpForm();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(nullSignUpForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 중복 이메일 테스트")
    @Test
//    @Disabled
    void emailReduplicationSignUpTest() throws Exception {
    	UsersRequestDto.SignUp signUpForm = signUpTestDto.createSignUpForm();
    	
    	String content = objectMapper.writeValueAsString(signUpForm);

    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(content)
    			)
    			.andExpect(status().isOk());

    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(content)
    			)
	    		.andExpect(status().isBadRequest());

    	assertThat(1L, is(usersRepository.count()));
    }
    
    @Test
    void profileSignUpTest() throws Exception {
    	UsersRequestDto.SignUp signUpFormByProfilesSignUp = signUpTestDto.createSignUpFormByProfilesSignUp();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpFormByProfilesSignUp))
    			)
    			.andExpect(status().isOk())
    			.andDo(print());
    	
    }
    
}