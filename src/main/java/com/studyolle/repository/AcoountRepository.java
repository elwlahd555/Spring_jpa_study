package com.studyolle.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.studyolle.repository.dto.Account;

@Transactional(readOnly = true)
public interface AcoountRepository extends JpaRepository<Account, Long>{

	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

}
