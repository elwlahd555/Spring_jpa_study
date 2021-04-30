package com.studyolle.account.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	

	public void processNewAccount(SignUpForm signUpForm) {
		Account newAccount = saveNewAccount(signUpForm);
		sendSignUpConfirmEmail(newAccount);
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
				"/check-email-tocken?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
		javaMailSender.send(mailMessage);
	}
}
