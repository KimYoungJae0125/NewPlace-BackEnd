package shop.newplace.Users.signUp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.util.CipherUtil;

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

	String loginEmail = "abcdefg@naver.com";
	String name = "테스터";
	String password = "abcdefg!@1";
	String mainPhoneNumber = "01012345678";
	String bankId = "01";
	String accountNumber = "12345678";
	
	
    @BeforeEach
    void setup() {
//    	this.mockMvc = MockMvcBuilders
//    				   .webAppContextSetup(this.context)
////    				   .addFilter(new CharacterEncodingFilter("UTF-8", true))
//    				   .alwaysDo(print())
//    				   .build();
    	System.out.println("테스트 시작");
    }
	
	
    @DisplayName("정상 회원가입 테스트")
    @Test
//    @Disabled
    void signupTest() throws Exception {
    	SignUpForm signupForm = SignUpForm.builder()
    									.name(name)
    									.loginEmail(loginEmail)
    									.password(password)
    									.passwordVerified(password)
    									.mainPhoneNumber(mainPhoneNumber)
    									.bankId(bankId)
    									.accountNumber(accountNumber)
    									.build();
    	
    	mockMvc.perform(post("/users")
			    			.contentType(MediaType.APPLICATION_JSON)
			    			.content(objectMapper.writeValueAsString(signupForm))
    						)
    					.andExpect(status().isOk())
    					.andDo(print());
    	
    	List<Users> usersList = usersRepository.findAll();
    	
    	Users users = usersList.get(0);
    	assertThat(CipherUtil.Email.decrypt(users.getLoginEmail())).isEqualTo(loginEmail);
    	assertThat(CipherUtil.Name.decrypt(users.getName())).isEqualTo(name);
    	assertThat(CipherUtil.Name.decrypt(users.getName())).isEqualTo(password);
    	
    	System.out.println("usersList : " + usersList);
    	
    }
    
    @DisplayName("JSON아닌 형식으로 파라미터 보내기")
    @Test
//    @Disabled
    void signupFailureTest() throws Exception {
    	
    	mockMvc.perform(post("/users")
    						.param("name", name)
    						.param("loginEmail", loginEmail)
    						.param("password", password)
    						.param("mainPhoneNumber ", mainPhoneNumber)
    						.param("bankId", bankId)
    						.param("accountNumber", accountNumber)
    			)
    	.andExpect(status().isBadRequest());
//    					.andDo(print());
    }
    
    @DisplayName("회원가입 Valid EmailFailure 테스트")
    @Test
//    @Disabled
    void signupValidEmailFailureTest() throws Exception {
    	
    	SignUpForm signupForm = SignUpForm.builder()
    			.name(name)
    			.loginEmail("abcdefg")
    			.password(password)
    			.passwordVerified(password)
    			.mainPhoneNumber(mainPhoneNumber)
    			.bankId(bankId)
    			.accountNumber(accountNumber)
    			.build();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signupForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 Password ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordFailureTest() throws Exception {
    	
    	SignUpForm signupForm = SignUpForm.builder()
    			.name(name)
    			.loginEmail(loginEmail)
    			.password("abcdefg")
    			.passwordVerified("abcdefg")
    			.mainPhoneNumber(mainPhoneNumber)
    			.bankId(bankId)
    			.accountNumber(accountNumber)
    			.build();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signupForm))
    			)
    	.andExpect(status().isBadRequest());
    }

    @DisplayName("회원가입 PasswordVerified ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPasswordVerifiedFailureTest() throws Exception {
    	
    	SignUpForm signupForm = SignUpForm.builder()
    			.name(name)
    			.loginEmail(loginEmail)
    			.password(password)
    			.passwordVerified("abcdefg")
    			.mainPhoneNumber(mainPhoneNumber)
    			.bankId(bankId)
    			.accountNumber(accountNumber)
    			.build();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signupForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 Phone ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidPhoneFailureTest() throws Exception {
    	
    	SignUpForm signupForm = SignUpForm.builder()
    			.name(name)
    			.loginEmail(loginEmail)
    			.password(password)
    			.passwordVerified(password)
    			.mainPhoneNumber("01601010")
    			.bankId(bankId)
    			.accountNumber(accountNumber)
    			.build();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signupForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }

    @DisplayName("회원가입 NULL ValidFailure 테스트")
    @Test
//    @Disabled
    void signupValidNullFailureTest() throws Exception {
    	
    	SignUpForm signupForm = SignUpForm.builder()
    			.name(null)
    			.loginEmail(null)
    			.password(null)
    			.passwordVerified(null)
    			.mainPhoneNumber(null)
    			.bankId(null)
    			.accountNumber(null)
    			.build();
    	
    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signupForm))
    			)
    	.andExpect(status().isBadRequest());
    	
    }
    
    @DisplayName("회원가입 중복 이메일 테스트")
    @Test
//    @Disabled
    void emailReduplicationSignUpTest() throws Exception {
    	
    	SignUpForm signupForm = SignUpForm.builder()
    			.name(name)
    			.loginEmail(loginEmail)
    			.password(password)
    			.passwordVerified(password)
    			.mainPhoneNumber(mainPhoneNumber)
    			.bankId(bankId)
    			.accountNumber(accountNumber)
    			.build();
    	
    	String content = objectMapper.writeValueAsString(signupForm);

    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(content)
    			);
    	
    	System.out.println("중복 가입");
    	

    	mockMvc.perform(post("/users")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(content)
    			)
//    	.andExpect((result)
//    			-> assertTrue(result.getResolvedException()
//    								.getClass()
//    								.isAssignableFrom(SignUpRuntimeException.class)
//    								)
//    			)
				.andDo(print());

    	List<Users> usersList = usersRepository.findAll();
    	
    	System.out.println("usersList : " + usersList);
    	
    }

}