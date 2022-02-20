package shop.newplace.Users.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.Users.token.JwtTokenProvider;
import shop.newplace.Users.token.model.dto.JwtTokenForm;
import shop.newplace.Users.token.model.entity.JwtRefreshToken;
import shop.newplace.common.mail.service.EmailAuthenticationService;
import shop.newplace.common.redis.service.RedisService;
import shop.newplace.common.util.CipherUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final ModelMapper modelMapper;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final RedisService redisService;
	
	private final EmailAuthenticationService emailAuthenticationService;
	
	@Transactional
	public void signUp(SignUpForm signUpForm) {
		System.out.println("setting before : " + signUpForm);
		signUpForm
			.setName(CipherUtil.Name.encrypt(signUpForm.getName()))
			.setLoginEmail(CipherUtil.Email.encrypt(signUpForm.getLoginEmail()))
			.setMainPhoneNumber(CipherUtil.Phone.encrypt(signUpForm.getMainPhoneNumber()))
			.setPassword(passwordEncoder.encode(signUpForm.getPassword()))
			.setBankId(CipherUtil.BankId.encrypt(signUpForm.getBankId()))
			.setAccountNumber(CipherUtil.AccountNumber.encrypt(signUpForm.getAccountNumber()))
			.setRoles(signUpForm.getAuthId());
		System.out.println("setting after : " + signUpForm);
		Users users = usersRepository.save(modelMapper.map(signUpForm, Users.class));
		emailAuthenticationService.sendEmailAuthentication(users.getId(), signUpForm.getLoginEmail());
	}
	
	@Transactional
	public JwtTokenForm logIn(LogInForm logInForm, HttpServletResponse response) {
		Users usersInfo = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(logInForm.getLoginEmail())).get();
		usersInfo.successLogin();
		String loginEmail = CipherUtil.Email.decrypt(usersInfo.getLoginEmail());
		usersRepository.save(usersInfo);
		String accessToken = jwtTokenProvider.createAccessToken(loginEmail, usersInfo.getAuthorities());
		String refreshToken = jwtTokenProvider.createRefreshToken(usersInfo.getId().toString(), usersInfo.getAuthorities());
		jwtTokenProvider.setHeaderAccessToken(response, accessToken);
		jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);
		List<Profiles> profilesList = profilesRepository.findAllByUsers(usersInfo);
		
		JwtTokenForm jwtForm = JwtTokenForm.builder()
								 .profilesList(profilesList)
		  						 .accesToken(accessToken)
		  						 .refreshToken(refreshToken)
								 .build();
//		redisService.setValues(refreshToken, loginEmail);
		
		JwtRefreshToken jwtRefreshToken = JwtRefreshToken.builder()
										  .loginEmail(loginEmail)
										  .expirationTime(100L * 60 * 60 * 24 * 7)
										  .refreshToken(refreshToken)
										  .build();
		
		redisService.setValues(jwtRefreshToken);
//		jwtRefreshTokenRedisRepository.save(new JwtRefreshToken(usersInfo.getId(), refreshToken));
		
		return jwtForm;
	}
	
}
