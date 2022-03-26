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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import shop.newplace.common.constant.Role;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.dto.ProfilesRequestDto;
import shop.newplace.users.model.dto.UsersRequestDto;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.model.repository.UsersRepository;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RequiredArgsConstructor	//test는 Autowired로
class SignUpTest {

	@Autowired
    MockMvc mockMvc;
    
	@Autowired
    UsersRepository usersRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
//	@Autowired
//	private WebApplicationContext context;

	String loginEmail = "uni0125@nplace.dooray.com";
	String name = "테스터";
	String password = "abcdefg!@1";
	String mainPhoneNumber = "01012345678";
	String bankId = "01";
	String accountNumber = "12345678";
	String authId = Role.USER.getValue();
	
	UsersRequestDto.SignUp signUpForm;
	
    @BeforeEach
    void setup() {
    	this.objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//    	this.mockMvc = MockMvcBuilders
//    				   .webAppContextSetup(this.context)
////    				   .addFilter(new CharacterEncodingFilter("UTF-8", true))
//    				   .alwaysDo(print())
//    				   .build();
    	System.out.println("========================================테스트 시작========================================");
    	signUpForm = UsersRequestDto.SignUp.builder()
        								   .name(name)
        								   .loginEmail(loginEmail)
        								   .password(password)
        								   .passwordVerified(password)
        								   .mainPhoneNumber(mainPhoneNumber)
        								   .bankId(bankId)
        								   .accountNumber(accountNumber)
        								   .emailVerified(true)
        								   .build();
    }
    
    @AfterEach
    void unSet() {
    	usersRepository.deleteAll();
    	System.out.println("========================================테스트 종료========================================");
    }

    @DisplayName("정상 회원가입 테스트")
    @Test
//    @Disabled
    void signupNormalTest() throws Exception {
    	System.out.println("signupNormalTest");
    	
    	mockMvc.perform(post("/users")
			    			.contentType(MediaType.APPLICATION_JSON)
			    			.content(objectMapper.writeValueAsString(signUpForm))
    						)
    					.andExpect(status().isOk());
//    					.andDo(print());
    	
    	List<Users> usersList = usersRepository.findAll();
    	
    	Users users = usersList.get(0);
    	assertThat(CipherUtil.Email.decrypt(users.getLoginEmail()), is(loginEmail));
    	assertThat(CipherUtil.Name.decrypt(users.getName()), is(name));
    }
    
    @DisplayName("JSON아닌 형식으로 파라미터 보내기")
    @Test
//    @Disabled
    void signupFailureTest() throws Exception {
    	System.out.println("signUpFailureTest");
    	
    	mockMvc.perform(post("/users")
    						.param("name", name)
    						.param("loginEmail", loginEmail)
    						.param("password", password)
    						.param("mainPhoneNumber ", mainPhoneNumber)
    						.param("bankId", bankId)
    						.param("accountNumber", accountNumber)
    						.param("authId", String.valueOf(authId))
    			)
    	.andExpect(status().isBadRequest());
    }
    
    @DisplayName("회원가입 Valid EmailFailure 테스트")
    @Test
//    @Disabled
    void signupValidEmailFailureTest() throws Exception {
    	System.out.println("signUpValidEmailFailureTest");
    	
    	signUpForm.setLoginEmail("abcdefg");
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 Password ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordFailureTest() throws Exception {
    	System.out.println("signUpValidPasswordFailureTest");
    	
    	signUpForm.setPassword("abcdefg")
    			  .setPasswordVerified("abcdefg");
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpForm))
    			)
    	.andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 PasswordVerified ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordVerifiedFailureTest() throws Exception {
    	System.out.println("signUpValidPasswordVerifiedFailureTest");
    	
    	signUpForm.setPasswordVerified("abcdefg");
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 Phone ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPhoneFailureTest() throws Exception {
    	System.out.println("signUpValidPhoneFailureTest");
    	
    	signUpForm.setMainPhoneNumber("01601010");
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 NULL ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidNullFailureTest() throws Exception {
    	System.out.println("signUpValidNullFailureTest");
    	
    	signUpForm.setName(null)
    			  .setLoginEmail(null)
    			  .setPassword(null)
    			  .setPasswordVerified(null)
    			  .setMainPhoneNumber(null)
    			  .setBankId(null)
    			  .setAccountNumber(null)
    			  .setEmailVerified(false);
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 중복 이메일 테스트")
    @Test
//    @Disabled
    void emailReduplicationSignUpTest() throws Exception {
    	System.out.println("emailReduplicationSignUpTest");
    	
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
    	System.out.println("profileSignUpTest");
    	
    	ProfilesRequestDto.SignUp profilesSignUp = ProfilesRequestDto.SignUp.builder()
																			.nickName("테스터")
																			.authId("2")
																			.build();
    	
    	signUpForm.setProfilesSignUp(profilesSignUp);
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signUpForm))
    			)
    			.andExpect(status().isOk())
    			.andDo(print());
    	
    }

}