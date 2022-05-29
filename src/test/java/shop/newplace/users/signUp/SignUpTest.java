package shop.newplace.users.signUp;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.common.constant.Role;
import shop.newplace.common.model.dto.UsersTestDto;
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
	
	private UsersTestDto usersTestDto = new UsersTestDto();
	
	private SignUpTestSnippet usersTestSnippet = new SignUpTestSnippet();

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
    	UsersRequestDto.SignUp signUpForm = usersTestDto.createSignUpForm();
    	
    	mockMvc.perform(post("/users")
			    			.contentType(MediaType.APPLICATION_JSON)
			    			.content(objectMapper.writeValueAsString(signUpForm))
    						)
    					.andExpect(status().isOk())
    					.andDo(apiDocumentUtils.createAPIDocument("user/signup", usersTestSnippet.SuccessPostRequest(), usersTestSnippet.SuccessPostResponse()));
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
    	UsersRequestDto.SignUp signUpForm = usersTestDto.createSignUpForm();
    	
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
    	System.out.println("signUpValidEmailFailureTest");
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(usersTestDto.createSignUpFormByWrongLoginEmail()))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 Password ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordFailureTest() throws Exception {
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(usersTestDto.createSignUpFormByWrongPassword()))
    			)
    	.andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 PasswordVerified ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordVerifiedFailureTest() throws Exception {
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(usersTestDto.createSignUpFormByWrongPasswordVerified()))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 Phone ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPhoneFailureTest() throws Exception {
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(usersTestDto.createSignUpFormByWrongMainPhoneNumber()))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 NULL ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidNullFailureTest() throws Exception {
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(usersTestDto.createNullSignUpForm()))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 중복 이메일 테스트")
    @Test
//    @Disabled
    void emailReduplicationSignUpTest() throws Exception {
    	UsersRequestDto.SignUp signUpForm = usersTestDto.createSignUpForm();
    	
    	String content = objectMapper.writeValueAsString(signUpForm);

    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(content)
    			)
    			.andExpect(status().isOk());
    	System.out.println("중복 가입");
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(content)
    			)
	    		.andExpect(status().isBadRequest());

    	assertThat(1L, is(usersRepository.count()));
    }
    
    @Test
    void profileSignUpTest() throws Exception {
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(usersTestDto.createSignUpFormByProfilesSignUp()))
    			)
    			.andExpect(status().isOk())
    			.andDo(print());
    	
    }
    
}