package shop.newplace.users.logIn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
<<<<<<< HEAD

=======
>>>>>>> pre/feature/2022-03-06_signup
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
<<<<<<< HEAD

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.users.model.dto.UsersDto;
import shop.newplace.users.repository.UsersRepository;
=======
import com.fasterxml.jackson.databind.ObjectMapper;
import shop.newplace.users.model.dto.UsersRequestDto;
import shop.newplace.users.model.repository.UsersRepository;
>>>>>>> pre/feature/2022-03-06_signup

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
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

<<<<<<< HEAD
    	UsersDto.RequestSignUp signUpForm = UsersDto.RequestSignUp.builder()
=======
    	UsersRequestDto.SignUp signUpForm = UsersRequestDto.SignUp.builder()
>>>>>>> pre/feature/2022-03-06_signup
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

<<<<<<< HEAD
    	UsersDto.RequestLogIn signInForm = UsersDto.RequestLogIn.builder()
    									  .loginEmail(loginEmail)
    									  .password(password)
    									  .build();
=======
    	UsersRequestDto.LogIn signInForm = UsersRequestDto.LogIn.builder()
                            									.loginEmail(loginEmail)
                            									.password(password)
                            									.build();
>>>>>>> pre/feature/2022-03-06_signup
    	
    	mockMvc.perform(post("/users/login")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(signInForm))
				)
			.andExpect(status().isOk())
			.andDo(print());
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