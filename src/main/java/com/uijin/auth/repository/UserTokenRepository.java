package com.uijin.auth.repository;

import com.uijin.auth.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {

}
