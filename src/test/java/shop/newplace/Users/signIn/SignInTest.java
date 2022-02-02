package shop.newplace.Users.signIn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.role.Role;
import shop.newplace.common.util.CipherUtil;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
class SignInTest {

	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
    private UsersRepository usersRepository;
    
	@Autowired
	ObjectMapper objectMapper;
	
    @BeforeEach
    public void setup() throws Exception {
    
    	String loginEmail = "abcdefg@naver.com";
    	String name = "테스터";
    	String password = "abcdefg!@#1";
    	String mainPhoneNumber = "01012345678";
    	String bankId = "01";
    	String accountNumber = "12345678";

    	password 		= 	passwordEncoder.encode(password);
    	loginEmail 		= 	CipherUtil.Email.encrypt(loginEmail);
    	name 			= 	CipherUtil.Name.encrypt(name);
    	mainPhoneNumber = 	CipherUtil.Phone.encrypt(mainPhoneNumber);
    	bankId 			= 	CipherUtil.BankId.encrypt(bankId);
    	accountNumber 	= 	CipherUtil.AccountNumber.encrypt(accountNumber);
    	
    	Users users = Users.builder()
    						.loginEmail(loginEmail)
    						.name(name)
    						.password(password)
    						.bankId(bankId)
    						.accountNumber(accountNumber)
    						.failCount(0)
    						.mainPhoneNumber(mainPhoneNumber)
    						.authId(Role.USER.getRoleValue())
    						.build();

    	Users result = usersRepository.save(users);
    	
    	System.out.println("users : " + result);
    	
    	
    }
	
	
    @DisplayName("정상 로그인 테스트")
    @Test
    void signInTest() throws Exception {
    	String loginEmail = "abcdefg@naver.com";
    	String password = "abcdefg!@#1";

    	SignInForm signInForm = SignInForm.builder()
    									  .loginEmail(loginEmail)
    									  .password(password)
    									  .build();
    	
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