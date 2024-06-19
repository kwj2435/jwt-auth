package com.uijin.auth.user.repository;

import com.uijin.auth.user.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {

}
