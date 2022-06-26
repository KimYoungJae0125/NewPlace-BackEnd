package shop.newplace.common.model.dto;

import shop.newplace.common.model.vo.UsersTestVo;
import shop.newplace.users.model.dto.UsersRequestDto;

public class LogInTestDto {

	private UsersTestVo usersTestVo = new UsersTestVo();
	
	public UsersRequestDto.LogIn createLogInForm() {
		return  UsersRequestDto.LogIn.builder()
									 .loginEmail(usersTestVo.getLoginEmail())
									 .password(usersTestVo.getPassword())
									 .build();
	}
	
}
