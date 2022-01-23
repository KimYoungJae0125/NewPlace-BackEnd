package shop.newplace.Account.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.web.servlet.MockMvc;
import shop.newplace.Account.model.dto.request.SignUpRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private SignUpRequestDto signUpRequestDto;

    @DisplayName("컨트롤러를 테스트해보아요")
    @Test
    void signup() throws Exception {

        mockMvc.perform(post("/signup")
                        .param("email", "saysthabout@gmail.com")
                        .param("password", "1234")
                        .param("name","곽진아")
                        .param("bankId", "Hankook")
                        .param("accountNumber", "110261980103")
                        .param("failCount", "0")
                        .param("accountExpired", "false")
                        .param("accountLocked", "false")
                        .param("lastLoginAt", LocalDateTime.now().toString())
                        .param("mainPhoneNumber", "01072137281")
                        .param("joinedAt", LocalDateTime.now().toString())
                        .param("emailVerified", "true"))
            .andExpect(status().isOk()) 
            .andDo(print());
    }

    @DisplayName("컨트롤러 테스트")
    @Test
    void test() throws Exception {

        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andDo(print());
    }

}