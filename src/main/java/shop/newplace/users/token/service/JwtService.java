package shop.newplace.users.token.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.dto.UsersDto;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.token.JwtTokenProvider;
import shop.newplace.users.token.model.dto.JwtDto;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtTokenProvider jwtTokenProvider;
	
	public UsersDto.ResponseInfo getUserInfo(JwtDto.AccessToken jwtAccessToken){
		Users users = (Users) jwtTokenProvider.getAuthentication(jwtAccessToken.getAccessToken()).getPrincipal();
		return UsersDto.ResponseInfo.builder()
									.userId(users.getId())
									.loginEmail(CipherUtil.Email.decrypt(users.getLoginEmail()))
									.name(CipherUtil.Name.decrypt(users.getName()))
									.mainPhoneNumber(CipherUtil.Phone.decrypt(users.getMainPhoneNumber()))
									.bankId(CipherUtil.BankId.decrypt(users.getBankId()))
									.accountNumber(CipherUtil.AccountNumber.decrypt(users.getAccountNumber()))
									.build();
	}
	
}
