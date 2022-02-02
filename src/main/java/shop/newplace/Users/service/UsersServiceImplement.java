package shop.newplace.Users.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.JwtForm;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.Users.token.JwtTokenProvider;
import shop.newplace.common.advice.exception.ValidFailureException;
import shop.newplace.common.role.Role;
import shop.newplace.common.util.CipherUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImplement implements UsersService {

	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final ModelMapper modelMapper;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	@Transactional
	public void signUp(SignUpForm signUpForm) {
		signUpForm
			.setName(CipherUtil.Name.encrypt(signUpForm.getName()))
			.setLoginEmail(CipherUtil.Email.encrypt(signUpForm.getLoginEmail()))
			.setMainPhoneNumber(CipherUtil.Phone.encrypt(signUpForm.getMainPhoneNumber()))
			.setPassword(passwordEncoder.encode(signUpForm.getPassword()))
			.setBankId(CipherUtil.BankId.encrypt(signUpForm.getBankId()))
			.setAccountNumber(CipherUtil.AccountNumber.encrypt(signUpForm.getAccountNumber()));
		usersRepository.save(modelMapper.map(signUpForm, Users.class));

//		Optional<Users> userLoginEmail = usersRepository.findByLoginEmail(signUpForm.getLoginEmail());



//		log.info("userLoginEmail : " + userLoginEmail);
//		log.info("encoder before : " + signUpForm.getPassword());
//		String name				 = CipherUtil.Name.encrypt(signUpForm.getName());
//		String loginEmail 		 = CipherUtil.Email.encrypt(signUpForm.getLoginEmail());
//		String mainPhoneNumber 	 = CipherUtil.Phone.encrypt(signUpForm.getMainPhoneNumber());
//		String encodedPassword 	 = passwordEncoder.encode(signUpForm.getPassword());
//		String bankId 			 = CipherUtil.BankId.encrypt(signUpForm.getBankId());
//		String accountNumber  	 = CipherUtil.AccountNumber.encrypt(signUpForm.getAccountNumber());

//		Users usersParam = Users.builder()
//								.name(name)
//								.loginEmail(loginEmail)
//								.password(encodedPassword)
//								.mainPhoneNumber(mainPhoneNumber)
//								.accountNonExpired(true)
//								.accountNonLocked(true)
//								.bankId(bankId)
//								.accountNumber(accountNumber)
//								.failCount(0)
//								.lastLoginTime(null)
//								.authId(Role.USER.getRoleValue())
//								.build();


		
/*
		ModelMapper modelMapper = new ModelMapper();
		
		modelMapper.typeMap(Users.class, userLoginEmail.getClass());
*/	
//		log.info("encoder after: " + encodedPassword);

//
//		log.info("==============================");
//		log.info(result.toString());
//		log.info("==============================");
//
////		accountEmail.orElseThrow(() -> new UsernameNotFoundException("not found : " + accountEmail));
//
//		return result;
	}
	
	@Override
	@Transactional
	public JwtForm signIn(SignInForm logInForm) {
		Users usersInfo = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(logInForm.getLoginEmail())).get();
		usersInfo.successLogin();

//		String name 			= CipherUtil.Name.decrypt(usersInfo.getName());
		String loginEmail 		= CipherUtil.Email.decrypt(usersInfo.getLoginEmail());
//		String mainPhoneNumber 	= CipherUtil.Phone.decrypt(usersInfo.getMainPhoneNumber());
//		String bankId 			= CipherUtil.BankId.decrypt(usersInfo.getBankId());
//		String accountNumber 	= CipherUtil.AccountNumber.decrypt(usersInfo.getAccountNumber());
		
		Users usersParam = modelMapper.map(usersInfo, Users.class);
		
		System.out.println("modelMapper Test : " + usersParam);
		
		//modelMapper
//		Users usersParam = Users.builder()
//						.id(usersInfo.getId())
//						.name(usersInfo.getName())
//						.loginEmail(usersInfo.getLoginEmail())
//						.password(usersInfo.getPassword())
//						.mainPhoneNumber(usersInfo.getMainPhoneNumber())
//						.accountNonExpired(true)
//						.accountNonLocked(true)
//						.bankId(usersInfo.getBankId())
//						.accountNumber(usersInfo.getAccountNumber())
//						.failCount(0)
//						.lastLoginTime(LocalDateTime.now())
//						.authId(usersInfo.getAuthId())
//						.build();
		
		usersRepository.save(usersParam);
		
//		Users users = Users.builder()
//						   .name(name)
//						   .loginEmail(loginEmail)
//						   .mainPhoneNumber(mainPhoneNumber)
//						   .bankId(bankId)
//						   .accountNumber(accountNumber)
//						   .build();
		
		String token = jwtTokenProvider.createToken(loginEmail, usersInfo.getAuthorities());
		
		List<Profiles> profilesList = profilesRepository.findAllByUsers(usersParam);
				
		JwtForm jwtForm = JwtForm.builder()
								 .profilesList(profilesList)
		  						 .token(token)
								 .build();
		
		return jwtForm;
	}
	
}
