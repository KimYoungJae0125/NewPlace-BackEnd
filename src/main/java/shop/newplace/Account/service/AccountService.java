package shop.newplace.Account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.newplace.Account.model.dto.request.SignUpRequestDto;
import shop.newplace.Account.model.dto.response.SignupResponseDto;
import shop.newplace.Account.model.entity.Account;
import shop.newplace.Account.repository.AccountRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public SignupResponseDto insertAccount(SignUpRequestDto signUpRequestDto){
        signUpRequestDto.setEncodedPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        Account account = signUpRequestDto.toEntity();
        accountRepository.save(account);
        return SignupResponseDto.builder()
                .userId(account.getUserId())
                .email(account.getEmail())
                .password(account.getPassword())
                .name(account.getName())
//                .bankId(account.getBankId())
                .accountNumber(account.getAccountNumber())
                .failCount(account.getFailCount())
                .accountExpired(account.isAccountExpired())
                .accountLocked(account.isAccountLocked())
                .lastLoginAt(account.getLastLoginAt())
                .mainPhoneNumber(account.getMainPhoneNumber())
                .joinedAt(account.getJoinedAt())
                .mainPhoneNumber(account.getMainPhoneNumber())
                .joinedAt(account.getJoinedAt())
                .build();
    }
}
