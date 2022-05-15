package shop.newplace.common.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import shop.newplace.users.exception.DisabledUsersException;
import shop.newplace.users.exception.ExpiredPasswordException;
import shop.newplace.users.exception.ExpiredUsersException;
import shop.newplace.users.exception.LockedUsersException;
import shop.newplace.users.exception.NotMatchPasswordException;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.repository.UsersRepository;
=======
import shop.newplace.users.advice.exception.DisabledUsersException;
import shop.newplace.users.advice.exception.ExpiredPasswordException;
import shop.newplace.users.advice.exception.ExpiredUsersException;
import shop.newplace.users.advice.exception.LockedUsersException;
import shop.newplace.users.advice.exception.NotMatchPasswordException;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.model.repository.UsersRepository;
>>>>>>> pre/feature/2022-03-06_signup

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final CustomUserDetailsService customUserDetailsService;

	private final PasswordEncoder passwordEncoder;
	
	private final UsersRepository usersRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String loginEmail = authentication.getName();
		String userPassword = authentication.getCredentials().toString();
		
		log.info("loginEmail : " + loginEmail);
		log.info("userPassword : " + userPassword);
		
		CustomUserDetails users = customUserDetailsService.loadUserByUsername(loginEmail);
		
		if (!users.isEnabled()) {
			throw new DisabledUsersException("사용 불가능한 계정입니다.", loginEmail);
		} 
		if (!users.isAccountNonLocked()) {
			throw new LockedUsersException("잠긴 계정입니다.", loginEmail);
		} 
		if(!passwordEncoder.matches(userPassword, users.getPassword())) {
			Users failLoginUsers = users.getUsers();
			failLoginUsers.failLogin();
			usersRepository.save(failLoginUsers);
			throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다", userPassword);
		} 
		if (!users.isAccountNonExpired()) {
			throw new ExpiredUsersException("계정이 만료되었습니다.", loginEmail);
		}
		if (!users.isCredentialsNonExpired()) {
			throw new ExpiredPasswordException("비밀번호가 만료되었습니다.", userPassword);
		}
		
		return new UsernamePasswordAuthenticationToken(
				users, "", users.getAuthorities());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {

		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
