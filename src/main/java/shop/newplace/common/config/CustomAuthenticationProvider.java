package shop.newplace.common.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.service.CustomUserDetailService;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final CustomUserDetailService customUserDetailService;
	
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String loginEmail = authentication.getName();
		String userPassword = (String) authentication.getCredentials();
		
		Users users = (Users) customUserDetailService.loadUserByUsername(loginEmail);
		
		if(users == null || !loginEmail.equals(users.getLoginEmail())
						 || !passwordEncoder.matches(userPassword, users.getPassword())) {
			
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
		} else if (!users.isAccountNonLocked()) {
			
			throw new LockedException("잠긴 계정입니다.");
		} else if (!users.isEnabled()) {
			
			throw new DisabledException("사용 불가능한 계정입니다.");
		} else if (!users.isCredentialsNonExpired()) {
			
			throw new CredentialsExpiredException("비밀번호가 만료되었습니다.");
		}
		
		Authentication customAuth = new UsernamePasswordAuthenticationToken(
									users, userPassword, users.getAuthorities());
		
		return customAuth;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {

		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	

}
