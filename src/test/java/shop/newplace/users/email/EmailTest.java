package shop.newplace.users.email;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.mail.model.dto.EmailResponseDto;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.model.repository.UsersRepository;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@DataJpaTest
class EmailTest {

	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
	@Autowired
    private UsersRepository usersRepository;
    
	@Autowired
	ObjectMapper objectMapper;
	
	String loginEmail = "emailTester@newPlace";
	
//    @BeforeEach
    public void setup() throws Exception {
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
    						.mainPhoneNumber(mainPhoneNumber)
//    						.authId(Role.USER.getValue())
    						.build();
    	Users result = usersRepository.save(users);
    }
    
    @Test
	void emailAuthenticationTest() throws Exception {
    	System.out.println("emailAuthenticationTest");
    	
		
    	EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder()
				  .loginEmail(loginEmail)
				  .build();

    	MvcResult mvcResult = mockMvc.perform(post("/email/authentication")
								    			.contentType(MediaType.APPLICATION_JSON)
								    			.content(objectMapper.writeValueAsString(emailDto))
								    			)
							    	 .andExpect(status().isOk())
							    	 .andDo(print())
							    	 .andReturn();
    	Object responseData = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class).get("data");
    	EmailResponseDto.Info emailInfo = objectMapper.readValue(objectMapper.writeValueAsString(responseData), EmailResponseDto.Info.class) ;
    	emailDto.setCertificationNumber(emailInfo.getCertificationNumber());

    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
    			)
    	.andExpect(status().isOk())
    	.andDo(print());
		
	}
    
    
    @DisplayName("틀린 인증번호 보내기")
    @Test
    void failEmailAuthenticationTest() throws Exception {
    	String certificationNumber = certificationNumber();

    	EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder()
    									  .loginEmail(loginEmail)
    									  .certificationNumber(certificationNumber)
    									  .build();
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
				)
			.andExpect(status().isOk())
			.andDo(print());
    }

    private String certificationNumber() {
		StringBuffer certificationNumber = new StringBuffer();
		IntStream.range(0, 6).forEach(i -> certificationNumber.append(new Random().ints(0, 9).findFirst().getAsInt()));
		return certificationNumber.toString();
    }
    
    @Test
    void failEmailParamter() throws Exception {
    	
    	EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder().loginEmail("").build();
    	
    	mockMvc.perform(get("/email/authentication")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(emailDto))
				)
			.andExpect(status().isBadRequest())
			.andDo(print());
    	
    }
    
}