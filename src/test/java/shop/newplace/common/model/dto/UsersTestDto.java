package shop.newplace.common.model.dto;

import shop.newplace.common.model.vo.UsersTestVo;
import shop.newplace.users.model.dto.UsersRequestDto;

public class UsersTestDto {

	private ProfilesTestDto profilesTestDto = new ProfilesTestDto();

	private UsersTestVo usersTestVo = new UsersTestVo();
	
	public UsersRequestDto.SignUp createSignUpForm(){
		return UsersRequestDto.SignUp.builder()
									 .name(usersTestVo.getName())
									 .loginEmail(usersTestVo.getLoginEmail())
									 .password(usersTestVo.getPassword())
									 .passwordVerified(usersTestVo.getPassword())
									 .mainPhoneNumber(usersTestVo.getMainPhoneNumber())
									 .bankId(usersTestVo.getBankId())
									 .accountNumber(usersTestVo.getAccountNumber())
									 .emailVerified(usersTestVo.getEmailVerified())
									 .build();
	}
	
	public UsersRequestDto.SignUp createSignUpFormByProfilesSignUp(){
		return UsersRequestDto.SignUp.builder()
									.name(usersTestVo.getName())
									.loginEmail(usersTestVo.getLoginEmail())
									.password(usersTestVo.getPassword())
									.passwordVerified(usersTestVo.getPassword())
									.mainPhoneNumber(usersTestVo.getMainPhoneNumber())
									.bankId(usersTestVo.getBankId())
									.accountNumber(usersTestVo.getAccountNumber())
									.emailVerified(usersTestVo.getEmailVerified())
									.profilesSignUp(profilesTestDto.createProfilesSignUpFormAuthByUser())
									.build();
	}

	public UsersRequestDto.SignUp createSignUpFormByWrongLoginEmail(){
		return UsersRequestDto.SignUp.builder()
									 .name(usersTestVo.getName())
									 .loginEmail("wrongLoginEmail")
									 .password(usersTestVo.getPassword())
									 .passwordVerified(usersTestVo.getPassword())
									 .mainPhoneNumber(usersTestVo.getMainPhoneNumber())
									 .bankId(usersTestVo.getBankId())
									 .accountNumber(usersTestVo.getAccountNumber())
									 .emailVerified(usersTestVo.getEmailVerified())
									 .build();
	}
	
	public UsersRequestDto.SignUp createSignUpFormByWrongPassword(){
		return UsersRequestDto.SignUp.builder()
				.name(usersTestVo.getName())
				.loginEmail(usersTestVo.getLoginEmail())
				.password("wrong")
				.passwordVerified("wrong")
				.mainPhoneNumber(usersTestVo.getMainPhoneNumber())
				.bankId(usersTestVo.getBankId())
				.accountNumber(usersTestVo.getAccountNumber())
				.emailVerified(usersTestVo.getEmailVerified())
				.build();
	}
	
	public UsersRequestDto.SignUp createSignUpFormByWrongPasswordVerified(){
		return UsersRequestDto.SignUp.builder()
				.name(usersTestVo.getName())
				.loginEmail(usersTestVo.getLoginEmail())
				.password(usersTestVo.getPassword())
				.passwordVerified("wrong")
				.mainPhoneNumber(usersTestVo.getMainPhoneNumber())
				.bankId(usersTestVo.getBankId())
				.accountNumber(usersTestVo.getAccountNumber())
				.emailVerified(usersTestVo.getEmailVerified())
				.build();
	}
	
	public UsersRequestDto.SignUp createSignUpFormByWrongMainPhoneNumber(){
		return UsersRequestDto.SignUp.builder()
				.name(usersTestVo.getName())
				.loginEmail(usersTestVo.getLoginEmail())
				.password(usersTestVo.getPassword())
				.passwordVerified(usersTestVo.getPassword())
				.mainPhoneNumber("090333")
				.bankId(usersTestVo.getBankId())
				.accountNumber(usersTestVo.getAccountNumber())
				.emailVerified(usersTestVo.getEmailVerified())
				.build();
	}
	
	public UsersRequestDto.SignUp createNullSignUpForm(){
		return UsersRequestDto.SignUp.builder()
									 .name(null)
									 .loginEmail(null)
									 .password(null)
									 .passwordVerified(null)
									 .mainPhoneNumber(null)
									 .bankId(null)
									 .accountNumber(null)
									 .emailVerified(false)
									 .build();
	}
	
	
	
	public UsersRequestDto.LogIn createLogInForm() {
		return  UsersRequestDto.LogIn.builder()
									 .loginEmail(usersTestVo.getLoginEmail())
									 .password(usersTestVo.getPassword())
									 .build();
	}
	
}
