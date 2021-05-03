package com.studyolle.account.service;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studyolle.repository.AccountRepository;
import com.studyolle.repository.dto.Account;
import com.studyolle.repository.dto.SignUpForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	

	private final AccountRepository accountRepository;
	private final JavaMailSender javaMailSender;
	private final PasswordEncoder passwordEncoder;
	
	

	@Transactional
	public Account processNewAccount(SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		sendSignUpConfirmEmail(newAccount);
		return newAccount;
	}
	
	public Account saveNewAccount(SignUpForm signUpForm) {
		Account account = Account.builder()
				.email(signUpForm.getEmail())
				.nickname(signUpForm.getNickname())
				.password(passwordEncoder.encode(signUpForm.getPassword()))
				.studyCreatedByWeb(true).studyEnrollmentResultByWeb(true).studyUpdatedByWeb(true).build();
		Account newAccount = accountRepository.save(account);

		newAccount.generateEmailCheckToken();
		return newAccount;
	}

	public void sendSignUpConfirmEmail(Account newAccount) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("스터디올래, 회원 가입 인증");
		mailMessage.setText(
				"/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
		javaMailSender.send(mailMessage);
	}

	public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                account.getNickname(),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
	}
}