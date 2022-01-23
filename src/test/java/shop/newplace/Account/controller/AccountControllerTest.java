package shop.newplace.Account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.newplace.Account.model.dto.request.SignUpRequestDto;
import shop.newplace.Account.model.entity.Account;
import shop.newplace.Account.repository.AccountRepository;

import java.time.LocalDateTime;

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

    @DisplayName("컨트롤러를 테스트해보아요")
    @Test
    void signup() throws Exception {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("saysthabout@gmail.com")
                .password("1234")
                .name("홍길동")
                .mainPhoneNumber("01012341234")
                .build();

        // when
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .with(csrf()))
                .andExpect(status().isOk());

        // then
        Account saveAccount = accountRepository.findByName("홍길동");
        assertNotNull(saveAccount);
    }

    @DisplayName("컨트롤러 테스트")
    @Test
    void test() throws Exception {

        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}