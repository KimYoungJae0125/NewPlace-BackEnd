package shop.newplace.users.token;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.common.security.CustomUserDetails;
import shop.newplace.common.security.CustomUserDetailsService;
import shop.newplace.common.util.RedisUtil;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("spring.jwt.secret")
	private String SECRET_KEY;
	
	private long accesTokenValidMilisecond = 100L * 60 * 60; //1시간 토큰 유효
	private long refreshTokenValidMilisecond = 100L * 60 * 60 * 24 * 7; //일주일 토큰 유효
	
	private final CustomUserDetailsService customUserDetailsService;
	
	private final RedisUtil redisService;
	
	private String type = "bearer ";
	

	@PostConstruct
	protected void init() {
		SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}
	
	private String createToken(String userId, String loginEmail, Collection<? extends GrantedAuthority> roles, long tokenValidMilisecond) {
		Claims claims = Jwts.claims().setSubject(userId);
		claims.put("loginEmail", loginEmail);
		claims.put("roles", roles);
		Date now = new Date();
		
		log.info("claims? : " + claims);
		
		return Jwts.builder()
					.setClaims(claims)	//데이터
					.setIssuedAt(now)	//토큰 발행일자
					.setExpiration(new Date(now.getTime() + tokenValidMilisecond))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY)	//암호화 알고리즘
					.compact();
	}
	
	@Transactional
	public Authentication getAuthentication(String token) {
		CustomUserDetails securityUsers = customUserDetailsService.loadUserByUsername(getLoginEmailByToken(token));
		return new UsernamePasswordAuthenticationToken(securityUsers.getUsers(), "", securityUsers.getAuthorities());
	}
	
	public String createAccessToken(String userId, String loginEmail, Collection<? extends GrantedAuthority> roles) {
		return createToken(userId, loginEmail, roles, accesTokenValidMilisecond);
	}

	public String createRefreshToken(String userId, Collection<? extends GrantedAuthority> roles) {
		return createToken(userId, null, null, refreshTokenValidMilisecond);
	}
	
	public String getUserIdByToken(String token) {
		return parserJwtsBody(token)
				.getSubject();
	}

	public String getLoginEmailByToken(String token) {
			return parserJwtsBody(token)
					.get("loginEmail")
					.toString();
	}
	
	public String resolveAccessToken(HttpServletRequest request) {
		return resolveToken(request.getHeader("authorization"));
	}

	public String resolveRefreshToken(HttpServletRequest request) {
		// TODO Header에 refreshToken을 건네지 않고 레디스에만 관리하는건 어떨까요? - 영재
		return resolveToken(request.getHeader("refreshToken"));
	}

	public String resolveRefreshToken(Long userId) {
		// TODO Header에 refreshToken을 건네지 않고 레디스에만 관리하는건 어떨까요? - 영재
		return redisService.getValues(userId);
	}
	
	private String resolveToken(String token) {
		if(token != null) {
			if(token.substring(0, 7).equals(type)) {
				token = token.substring(7);
			}
			return token;
		} 
		return null;
	}
	
	public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("authorization", type + accessToken);
	}

	public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
		response.setHeader("refreshToken", type + refreshToken);
	}
	
	public boolean existsRefreshTokenByUserId(Long userId) {
		return redisService.getValues(userId) != null;
//		return jwtRefreshTokenRedisRepository.existsByRefreshToken(refreshToken);
	}
	
	public boolean validateToken(String token) {
		try {
			return !parserJwts(token).getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}
	
	private Jws<Claims> parserJwts(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
	}
	
	private Claims parserJwtsBody(String token) {
//		if(validateToken(token)) {
			return parserJwts(token).getBody();
//		} else {
//			throw new NotFoundUsersException("토큰없음" , "");
//		}
	}
	
	
} 