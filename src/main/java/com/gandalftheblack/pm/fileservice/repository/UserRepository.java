package com.gandalftheblack.pm.fileservice.repository;

import com.gandalftheblack.pm.fileservice.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findUserByEmail(String email);
}
