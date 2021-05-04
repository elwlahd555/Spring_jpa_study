package com.studyolle.account.controller;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.studyolle.repository.dto.Account;

import lombok.Getter;

@Getter
public class UserAccount extends User{
	
	private Account account;
	
	public UserAccount(Account account) {
		super(account.getNickname(), account.getPassword(),List.of(new SimpleGrantedAuthority("ROLE_USER")));
		this.account=account;
	}

}
