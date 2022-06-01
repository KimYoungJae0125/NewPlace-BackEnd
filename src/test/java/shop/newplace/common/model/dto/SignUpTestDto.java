package shop.newplace.common.model.dto;

import shop.newplace.common.model.vo.UsersTestVo;
import shop.newplace.users.model.dto.ProfilesRequestDto;
import shop.newplace.users.model.dto.UsersRequestDto;

public class SignUpTestDto {

	private ProfilesTestDto profilesTestDto = new ProfilesTestDto();

	private UsersTestVo usersTestVo = new UsersTestVo();
	
	public UsersRequestDto.SignUp createSignUpForm(){
		return builderSignUpForm(usersTestVo.getName()
							   , usersTestVo.getLoginEmail()
							   , usersTestVo.getPassword()
							   , usersTestVo.getPassword()
							   , usersTestVo.getMainPhoneNumber()
							   , usersTestVo.getBankId()
							   , usersTestVo.getAccountNumber()
							   , usersTestVo.getEmailVerified()
							   , null
							   );
	}
	
	public UsersRequestDto.SignUp createSignUpFormByProfilesSignUp(){
		return builderSignUpForm(usersTestVo.getName()
							   , usersTestVo.getLoginEmail()
							   , usersTestVo.getPassword()
							   , usersTestVo.getPassword()
							   , usersTestVo.getMainPhoneNumber()
							   , usersTestVo.getBankId()
							   , usersTestVo.getAccountNumber()
							   , usersTestVo.getEmailVerified()
							   , profilesTestDto.createProfilesSignUpFormAuthByUser()
							   );
	}

	public UsersRequestDto.SignUp createSignUpFormByWrongLoginEmail(){
		return builderSignUpForm(usersTestVo.getName()
							   , "wrongLoginEmail"
							   , usersTestVo.getPassword()
							   , usersTestVo.getPassword()
							   , usersTestVo.getMainPhoneNumber()
							   , usersTestVo.getBankId()
							   , usersTestVo.getAccountNumber()
							   , usersTestVo.getEmailVerified()
							   , null
							   );		
	}
	
	public UsersRequestDto.SignUp createSignUpFormByWrongPassword(){
		return builderSignUpForm(usersTestVo.getName()
							   , usersTestVo.getLoginEmail()
							   , "wrongPassword"
							   , "wrongPassword"
							   , usersTestVo.getMainPhoneNumber()
							   , usersTestVo.getBankId()
							   , usersTestVo.getAccountNumber()
							   , usersTestVo.getEmailVerified()
							   , null
							   );		
	}
	
	public UsersRequestDto.SignUp createSignUpFormByWrongPasswordVerified(){
		return builderSignUpForm(usersTestVo.getName()
							   , usersTestVo.getLoginEmail()
							   , usersTestVo.getPassword()
							   , "wrongPassword"
							   , usersTestVo.getMainPhoneNumber()
							   , usersTestVo.getBankId()
							   , usersTestVo.getAccountNumber()
							   , usersTestVo.getEmailVerified()
							   , null
							   );	
	}
	
	public UsersRequestDto.SignUp createSignUpFormByWrongMainPhoneNumber(){
		return builderSignUpForm(usersTestVo.getName()
							   , usersTestVo.getLoginEmail()
							   , usersTestVo.getPassword()
							   , usersTestVo.getPassword()
							   , "090333"
							   , usersTestVo.getBankId()
							   , usersTestVo.getAccountNumber()
							   , usersTestVo.getEmailVerified()
							   , null
							   );	
	}
	
	public UsersRequestDto.SignUp createNullSignUpForm(){
		return builderSignUpForm(null
							   , null
							   , null
							   , null
							   , null
							   , null
							   , null
							   , false
							   , null
							   );	
	}
	
	
	private UsersRequestDto.SignUp builderSignUpForm(String name, String loginEmail, String password, String passwordVerfied, String mainPhoneNumber, String bankId, String accountNumber, Boolean emailVerified, ProfilesRequestDto.SignUp profilesSignUp){
		return UsersRequestDto.SignUp.builder()
									 .name(name)
									 .loginEmail(loginEmail)
									 .password(password)
									 .passwordVerified(passwordVerfied)
									 .mainPhoneNumber(mainPhoneNumber)
									 .bankId(bankId)
									 .accountNumber(accountNumber)
									 .emailVerified(emailVerified)
									 .profilesSignUp(profilesSignUp)
									 .build();
	}
	
}
