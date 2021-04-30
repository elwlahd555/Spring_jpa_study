package com.studyolle.account.controller;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.studyolle.repository.AcoountRepository;
import com.studyolle.repository.dto.SignUpForm;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor		//final 인 생성자를 자동으로 만들어 준다.
public class SignUpFormValidator implements Validator{
	
	private final AcoountRepository accountRopository;
	

	@Override
	public boolean supports(Class<?> aClass) {
		return aClass.isAssignableFrom(SignUpForm.class);
		
	}
	
	@Override
	public void validate(Object o, Errors errors) {
		SignUpForm signUpForm=(SignUpForm)errors;
		if(accountRopository.existsByEmail(signUpForm.getEmail())) {
			errors.rejectValue("email","invalid.email",new Object[] {signUpForm.getEmail()},"이미 사용중인 이메일입니다.");
		}
		if(accountRopository.existsByNickname(signUpForm.getNickname())) {
			errors.rejectValue("nickname","invalid.nickname",new Object[] {signUpForm.getEmail()},"이미 사용중인 닉네임입니다.");
		}
	}
}
