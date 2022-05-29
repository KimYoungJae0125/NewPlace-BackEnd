package shop.newplace.common.model.vo;

import lombok.Getter;

@Getter
public class UsersTestVo {
	private String name = "테스터";
	private String loginEmail = "newPlaceTest@email";
	private String password = "abcdefg!@1";
	private String mainPhoneNumber = "01012345678";
	private String bankId = "01"; 
	private String accountNumber = "12345678";
	private Boolean emailVerified = true;
}
