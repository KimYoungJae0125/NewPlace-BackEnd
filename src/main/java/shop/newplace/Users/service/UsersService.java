package shop.newplace.Users.service;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.JwtTokenForm;
import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.JwtRefreshToken;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.JwtRefreshTokenRedisRepository;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.Users.token.JwtTokenProvider;
import shop.newplace.common.role.Role;
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
	
	private final JwtRefreshTokenRedisRepository jwtRefreshTokenRedisRepository;
	
	@Transactional
	public void signUp(SignUpForm signUpForm) {
		signUpForm
			.setName(CipherUtil.Name.encrypt(signUpForm.getName()))
			.setLoginEmail(CipherUtil.Email.encrypt(signUpForm.getLoginEmail()))
			.setMainPhoneNumber(CipherUtil.Phone.encrypt(signUpForm.getMainPhoneNumber()))
			.setPassword(passwordEncoder.encode(signUpForm.getPassword()))
			.setBankId(CipherUtil.BankId.encrypt(signUpForm.getBankId()))
			.setAccountNumber(CipherUtil.AccountNumber.encrypt(signUpForm.getAccountNumber()))
			.setRoles(Role.USER.getValue());
		usersRepository.save(modelMapper.map(signUpForm, Users.class));
	}
	
	@Transactional
	public JwtTokenForm signIn(LogInForm logInForm) {
		Users usersInfo = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(logInForm.getLoginEmail())).get();
		usersInfo.successLogin();
		String loginEmail = CipherUtil.Email.decrypt(usersInfo.getLoginEmail());
		usersRepository.save(usersInfo);
		String accessToken = jwtTokenProvider.createAccessToken(loginEmail, usersInfo.getAuthorities());
		String refreshToken = jwtTokenProvider.createRefreshToken(usersInfo.getId().toString(), usersInfo.getAuthorities());
		List<Profiles> profilesList = profilesRepository.findAllByUsers(usersInfo);
		
		
		JwtTokenForm jwtForm = JwtTokenForm.builder()
								 .profilesList(profilesList)
		  						 .accesToken(accessToken)
		  						 .refreshToken(refreshToken)
								 .build();
		jwtRefreshTokenRedisRepository.save(new JwtRefreshToken(usersInfo.getId(), refreshToken));
		return jwtForm;
	}
	
}
