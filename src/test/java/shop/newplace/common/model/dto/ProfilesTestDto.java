package shop.newplace.common.model.dto;

import shop.newplace.common.model.vo.AuthTestVo;
import shop.newplace.common.model.vo.ProfilesTestVo;
import shop.newplace.users.model.dto.ProfilesRequestDto;

public class ProfilesTestDto {
	
	private ProfilesTestVo profilesTestVo = new ProfilesTestVo();
	private AuthTestVo authTestVo = new AuthTestVo();
	
	public ProfilesRequestDto.SignUp createProfilesSignUpFormAuthByUser(){
		return ProfilesRequestDto.SignUp.builder()
										.nickName(profilesTestVo.getNickNmae())
										.authId(authTestVo.getUserAuthId())
										.build();
	}

}
