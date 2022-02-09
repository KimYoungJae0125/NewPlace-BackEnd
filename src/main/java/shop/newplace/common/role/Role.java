package shop.newplace.common.role;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {
	  USER(1, "ROLE_USER")
	, PARTNER(2, "ROLE_PARTNER")
	, ADMIN(3, "ROLE_ADMIN");

	private int value;
	private String name;
	
	public static Role findByValue(int value) {
		return Arrays.stream(Role.values())
				.filter(role -> role.value == value)
				.findAny()
				.orElse(null);
	}

	public static Role findByName(String name) {
		return Arrays.stream(Role.values())
				.filter(role -> role.name.equals(name))
				.findAny()
				.orElse(null);
	}
	
	public static int getValueByName(String name) {
		Role role = findByName(name);
		return role == null ? null : role.value;
	}

	public static String getNameByValue(int value) {
		Role role = findByValue(value);
		return role == null ? null : role.name;
	}
	

}
