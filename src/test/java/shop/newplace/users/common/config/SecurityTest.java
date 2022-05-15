package shop.newplace.users.common.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.newplace.common.security.CustomOnceRequestFilter;
import shop.newplace.common.security.CustomUserDetailsService;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.common.util.RedisUtil;
import shop.newplace.users.model.dto.ProfilesRequestDto;
import shop.newplace.users.model.dto.UsersRequestDto;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.UsersRepository;
import shop.newplace.users.service.UsersService;
import shop.newplace.users.token.JwtTokenProvider;
import shop.newplace.users.token.model.dto.JwtDto;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(value = Lifecycle.PER_CLASS)
//@WebMvcTest(JwtController.class)
//@RunWith(SpringRunner.class)
//@DataJpaTest
class SecurityTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UsersRepository usersRepository;

	private String type = "bearer ";
	
	private Users result;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private MockHttpServletRequest request;
	
	@Autowired
	private MockHttpServletResponse response;
	
	@Autowired
	private RedisUtil redisService;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	String loginEmail = "tokenTest@email.com";
	String name = "테스터";
	String password = "abcdefg!@#1";
	String mainPhoneNumber = "01012345678";
	String bankId = "01";
	String accountNumber = "12345678";
	
	@BeforeEach
	public void setUpEach(WebApplicationContext applicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.addFilter(new CustomOnceRequestFilter(jwtTokenProvider))
				.addFilter(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();
	}
	
    @BeforeAll
    public void setup() throws Exception {
    	ProfilesRequestDto.SignUp profilesSignUp = ProfilesRequestDto.SignUp.builder()
                                                            				.nickName("테스터")
                                                            				.authId("2")
                                                            				.build();
    	
    	UsersRequestDto.SignUp signUpForm = UsersRequestDto.SignUp.builder()
																.name(name)
																.loginEmail(loginEmail)
																.password(password)
																.mainPhoneNumber(mainPhoneNumber)
																.bankId(bankId)
																.accountNumber(accountNumber)
																.profilesSignUp(profilesSignUp)
																.build();
    	usersService.signUp(signUpForm);
    	
    }
    
    @AfterAll
    void unSet() {
    	usersRepository.deleteAll();
		SecurityContextHolder.clearContext();
    }
	
	
    @DisplayName("ROLE_USER 테스트")
    @Test
//    @WithUserDetails("ROLE_USER")
    //SecurityContextHolder에 정보가 입력 됨
//    @WithMockUser(username = "abcdefg@naver.com", roles = {"USER"})
    @Transactional(readOnly = true)
    void logInTest() throws Exception {
    	result = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(loginEmail)).get();
    	
    	UsersRequestDto.LogIn loginForm = UsersRequestDto.LogIn.builder()
    											.loginEmail(loginEmail)
    											.password(password)
    											.build();
    	
    	mockMvc.perform(post("/users/login")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(loginForm)))
    			.andDo(handler -> {
    						request.addHeader("authorization", handler.getResponse().getHeader("authorization"));
    						request.addHeader("refreshToken", handler.getResponse().getHeader("refreshToken"));
							});
    	
  
    	
    	String accessToken =  jwtTokenProvider.resolveAccessToken(request);
    	String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
    	
		jwtTokenProvider.setHeaderAccessToken(response, accessToken);
//		SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(requestHeaderToken));
		
//		 = jwtTokenProvider.createRefreshToken(result.getId().toString(), securityUsers.getAuthorities());
		JwtDto.RefreshToken jwtRefreshToken = JwtDto.RefreshToken.builder()
																  .id(result.getId())
																  .expirationTime(100L * 60 * 60 * 24 * 7)
																  .refreshToken(refreshToken)
																  .build();
		
		redisService.setValues(jwtRefreshToken);
		
    	
    	System.out.println(SecurityContextHolder.getContext().getAuthentication());
    	System.out.println("authorization Test : " + request.getHeader("authorization"));
    	System.out.println("refreshToken Test : " + request.getHeader("refreshToken"));
    	
//    	assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities(), is(test));
    	mockMvc.perform(get("/jwt")
    					.header("authorization", request.getHeader("authorization"))
    					.header("refreshToken", request.getHeader("refreshToken"))
//    					.contentType(MediaType.APPLICATION_JSON)
//    					.content(objectMapper.writeValueAsString(httpHeaders))
    					)
    			.andExpect(status().isOk());
    	
		redisService.deleteValues(result.getId());

    }
    
    @Disabled
    void test() {
    	String[] roles = {"ROLE_USER"};
    	assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities(), is(""));
    }
}