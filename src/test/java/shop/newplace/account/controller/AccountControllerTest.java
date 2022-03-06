package shop.newplace.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import shop.newplace.account.model.dto.request.SignUpRequestDto;
import shop.newplace.account.model.entity.Account;
import shop.newplace.account.repository.AccountRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원가입 테스트")
    @Test
    void signup() throws Exception {
        // given
        String email = "saysthabout@gmail.com";
        String rawPassword = "1234";
        String name = "홍길동";
        String mainPhoneNumber = "01012341234";

        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email(email)
                .password(rawPassword)
                .name(name)
                .mainPhoneNumber(mainPhoneNumber)
                .build();

        // when
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .with(csrf()))
                .andExpect(status().isOk());
        Account account = accountRepository.findByName("홍길동");

        // then
        assertAll(
                () -> assertNotEquals(rawPassword, account.getPassword()),
                () -> assertTrue(passwordEncoder.matches(rawPassword, account.getPassword())),
                () -> assertEquals(email, account.getEmail()),
                () -> assertEquals(name, account.getName()),
                () -> assertEquals(mainPhoneNumber, account.getMainPhoneNumber())
        );
    }

    @DisplayName("컨트롤러 테스트")
    @Test
    void test() throws Exception {

        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}