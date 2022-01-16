package shop.newplace.Users.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.role.Role;

@Slf4j
@Service
public class UsersServiceImplement implements UsersService {
	
	Logger logger = LoggerFactory.getLogger(UsersServiceImplement.class);
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public Users loadUserByUsername(String loginEmail) throws UsernameNotFoundException {
		 Users usersInfo = usersRepository.findByLoginEmail(loginEmail)
							  .orElseThrow(() -> new UsernameNotFoundException(loginEmail + "은 등록되지 않은 이메일입니다."));
	
		 Users usersParam = Users.builder()
				 				 .userId(usersInfo.getUserId())
								 .name(usersInfo.getName())
								 .loginEmail(usersInfo.getLoginEmail())
								 .password(usersInfo.getPassword())
								 .mainPhoneNumber(usersInfo.getMainPhoneNumber())
								 .accountNonExpired(true)
								 .accountNonLocked(true)
								 .bankId(usersInfo.getBankId())
								 .accountNumber(usersInfo.getAccountNumber())
								 .failCount("0")
								 .lastLoginTime(LocalDateTime.now())
								 .authId(usersInfo.getAuthId())
								 .build();
		 
		 usersRepository.save(usersParam);
		 
		 return usersParam;
	}
	
	
	@Override
	@Transactional
	public int signUp(SignUpForm signUpForm) {
		Optional<Users> userLoginEmail = usersRepository.findByLoginEmail(signUpForm.getLoginEmail());
										
		logger.info("userLoginEmail : " + userLoginEmail);
		logger.info("encoder before : " + signUpForm.getPassword());
		String encodedPassword = passwordEncoder.encode(signUpForm.getPassword());
		Users usersParam = Users.builder()
									.name(signUpForm.getName())
									.loginEmail(signUpForm.getLoginEmail())
									.password(encodedPassword)
									.mainPhoneNumber(signUpForm.getMainPhoneNumber())
									.accountNonExpired(true)
									.accountNonLocked(true)
									.bankId(signUpForm.getBankId())
									.accountNumber(signUpForm.getAccountNumber())
									.failCount("0")
									.lastLoginTime(null)
									.authId(Role.USER.getRoleValue())
									.build();
		logger.info("encoder after: " + encodedPassword);
		Users result = usersRepository.save(usersParam);
		
		logger.info("==============================");
		logger.info(result.toString());
		logger.info("==============================");
		
//		accountEmail.orElseThrow(() -> new UsernameNotFoundException("not found : " + accountEmail));
		
		return 0;
	}
	
	@Override
	public String login(LogInForm logInForm) {
		Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(logInForm.getLoginEmail()
															  , logInForm.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Users principal = (Users) authentication.getPrincipal();
		
		LogInForm logInFormParam = LogInForm.builder()
								.loginEmail(logInForm.getLoginEmail())
								.password(passwordEncoder.encode(logInForm.getPassword()))
								.build();
		
		return principal.getUsername();
		
	}
	
	

}
