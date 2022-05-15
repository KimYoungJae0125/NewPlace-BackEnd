package shop.newplace.users.token.service;

import javax.servlet.http.HttpServletRequest;
<<<<<<< HEAD

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.dto.UsersDto;
=======
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.dto.UsersResponseDto;
>>>>>>> pre/feature/2022-03-06_signup
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.token.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtTokenProvider jwtTokenProvider;
	
	public UsersResponseDto.Info getUserInfo(HttpServletRequest request){
		Users users = (Users) jwtTokenProvider.getAuthentication(jwtTokenProvider.resolveAccessToken(request)).getPrincipal();
		return UsersResponseDto.Info.builder()
									.userId(users.getId())
									.loginEmail(CipherUtil.Email.decrypt(users.getLoginEmail()))
									.name(CipherUtil.Name.decrypt(users.getName()))
									.mainPhoneNumber(CipherUtil.Phone.decrypt(users.getMainPhoneNumber()))
									.bankId(CipherUtil.BankId.decrypt(users.getBankId()))
									.accountNumber(CipherUtil.AccountNumber.decrypt(users.getAccountNumber()))
									.build();
	}
	
}
