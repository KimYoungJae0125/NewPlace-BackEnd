package shop.newplace.users.token;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import shop.newplace.common.security.CustomUserDetails;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.common.util.RedisUtil;
import shop.newplace.users.model.dto.UsersDto;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.UsersRepository;
import shop.newplace.users.service.UsersService;
import shop.newplace.users.token.model.dto.JwtDto;

@SpringBootTest(properties = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(value = Lifecycle.PER_CLASS)
public class TokenTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private MockHttpServletRequest request;
	
	@Autowired
	private MockHttpServletResponse response;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private RedisUtil redisService;
	
	private String type = "bearer ";
	
	private Users result;
	
	private CustomUserDetails securityUsers;
	
	private String loginEmail = "tokenTest@email.com";
	
	@BeforeAll
	void setUp() {
    	String name = "테스터";
    	String password = "abcdefg!@#1";
    	String mainPhoneNumber = "01012345678";
    	String bankId = "01";
    	String accountNumber = "12345678";
    	UsersDto.RequestSignUp userSignUp = UsersDto.RequestSignUp.builder()
    									.loginEmail(loginEmail)
    									.name(name)
    									.password(password)
    									.mainPhoneNumber(mainPhoneNumber)
    									.bankId(bankId)
    									.accountNumber(accountNumber)
    									.build();
    	usersService.signUp(userSignUp);	
    	result = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(loginEmail)).get();
    	
		Set<String> roles = new HashSet<String>();
		roles.add("1");
		securityUsers  = CustomUserDetails.builder()
									      .users(result)
									      .roles(roles)
									      .build();
	}
	
    @AfterAll
    void unSet() {
    	usersRepository.deleteAll();
    }
	
    @DisplayName("AccessToken 확인")
	@Test
	void headerAccessToken() {
    	String [] test2 = {"USERS", "PARTNER"};
		String anyAuthorities = StringUtils.arrayToDelimitedString(test2, "','ROLE_");
		String test = "hasAnyRole('ROLE_" + anyAuthorities + "')";
		System.out.println(test);
    	
		String accessToken = jwtTokenProvider.createAccessToken(result.getId().toString(), loginEmail, securityUsers.getAuthorities());
		
		request.addHeader("authorization", type + accessToken);
		String requestHeaderToken = jwtTokenProvider.resolveAccessToken(request);
		jwtTokenProvider.setHeaderAccessToken(response, accessToken);
		SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(requestHeaderToken));

		assertThat(jwtTokenProvider.getAuthentication(accessToken), is(SecurityContextHolder.getContext().getAuthentication()));
		assertThat(jwtTokenProvider.getAuthentication(response.getHeader("authorization").substring(7)), is(SecurityContextHolder.getContext().getAuthentication()));
		assertThat(accessToken, is(requestHeaderToken));
		assertThat(type + accessToken, is(response.getHeader("authorization")));
		assertThat(accessToken, is(jwtTokenProvider.resolveAccessToken(request)));
		SecurityContextHolder.clearContext();
	}
	
    @DisplayName("RefreshToken 확인")
	@Test
	void headerRefreshToken() {
		String refreshToken = jwtTokenProvider.createRefreshToken(result.getId().toString(), securityUsers.getAuthorities());
		JwtDto.RefreshToken jwtRefreshToken = JwtDto.RefreshToken.builder()
																  .id(result.getId())
																  .expirationTime(100L * 60 * 60 * 24 * 7)
																  .refreshToken(refreshToken)
																  .build();
		
		redisService.setValues(jwtRefreshToken);
		assertThat(jwtTokenProvider.validateToken(refreshToken), is(true));
		assertThat(redisService.getValues(result.getId()), is(refreshToken));
		assertThat(jwtTokenProvider.existsRefreshTokenByUserId(result.getId()), is(true));
		
		redisService.deleteValues(result.getId());

	}
	
    @DisplayName("AccessToken은 만료 됐으나 RefreshToken 존재")
    @Test
    void expirationAccessTokenButExistenceRefershToken() {
    	String expirationAccessToken = expirationToken();
    	assertThat(jwtTokenProvider.validateToken(expirationAccessToken), is(false));
    	assertThat(jwtTokenProvider.validateToken(expirationAccessToken), is(not(nullValue())));
		String refreshToken = jwtTokenProvider.createRefreshToken(result.getId().toString(), securityUsers.getAuthorities());
		JwtDto.RefreshToken jwtRefreshToken = JwtDto.RefreshToken.builder()
																  .id(result.getId())
																  .expirationTime(100L * 60 * 60 * 24 * 7)
																  .refreshToken(refreshToken)
																  .build();
		
		redisService.setValues(jwtRefreshToken);
		System.out.println("만료 토큰 : " + expirationAccessToken);
		//refreshToken은 어디에...있어야하나
		System.out.println(Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString("spring.jwt.secret".getBytes())).parseClaimsJws(expirationAccessToken));
		String userId = jwtTokenProvider.getUserIdByToken(expirationAccessToken);
		String resolveRefreshToken = jwtTokenProvider.resolveRefreshToken(Long.valueOf(userId));
		assertThat(refreshToken, is(resolveRefreshToken));
		
		redisService.deleteValues(result.getId());
    }

    private String expirationToken() {
    	String SECRET_KEY = Base64.getEncoder().encodeToString("spring.jwt.secret".getBytes());

		Claims claims = Jwts.claims().setSubject(String.valueOf(result.getId()));
		claims.put("loginEmail", loginEmail);
		Date now = new Date();
		
		return Jwts.builder()
					.setClaims(claims)	//데이터
					.setIssuedAt(now)	//토큰 발행일자
					.setExpiration(new Date(now.getTime()))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY)	//암호화 알고리즘
					.compact();
    }
    
}
