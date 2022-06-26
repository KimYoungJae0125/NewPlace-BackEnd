package shop.newplace.unit.common;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.users.model.dto.UsersRequestDto;


@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(value = Lifecycle.PER_CLASS)
//@WebMvcTest(JwtController.class)
//@RunWith(SpringRunner.class)
//@DataJpaTest
class CorsTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	String loginEmail = "tokenTest@email";
	String name = "테스터";
	String password = "abcdefg!@#1";
	String mainPhoneNumber = "01012345678";
	String bankId = "01";
	String accountNumber = "12345678";
	
//	@BeforeEach
//	public void setUpEach(WebApplicationContext applicationContext) {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
//				.apply(SecurityMockMvcConfigurers.springSecurity())
//				.alwaysDo(print())
//				.build();
//	}
//	
//    @BeforeAll
//    public void setup() throws Exception {
//    	
//    }
//    
//    @AfterAll
//    void unSet() {
//    }
	
	UsersRequestDto.SignUp signUpForm;
	
	
    @DisplayName("CORS 테스트")
    @Test
    @Disabled
    void corsTest() throws Exception {
    	
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
    	
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/users")
    																.contentType(MediaType.APPLICATION_JSON)
    																.content(objectMapper.writeValueAsString(signUpForm))
    																.accept(MediaType.APPLICATION_JSON)
    																.header("Access-Control_Request-Method", "POST")
    																.header("origin", "https://localhost:3000");
    	
    	mockMvc.perform(builder)
    			.andExpect(status().isOk());
    }
    
}