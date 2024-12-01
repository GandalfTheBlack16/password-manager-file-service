package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoUserRepositoryTest {
    @Mock
    private MongoTemplate mongoTemplate;
    @InjectMocks
    private MongoUserRepository mongoUserRepository;

    @Test
    void shouldReturnAnEmptyOptionalIfNotResults() {
        when(mongoTemplate.findOne(any(), any())).thenReturn(null);
        Optional<UserEntity> userEntity = mongoUserRepository.findUserByEmail("test");
        verify(mongoTemplate, times(1))
                .findOne(Query.query(Criteria.where("email").is("test")), UserEntity.class);
        assertTrue(userEntity.isEmpty());
    }

    @Test
    void shouldReturnAPresentOptionalIfResults() {
        when(mongoTemplate.findOne(any(), any())).thenReturn(UserEntity.builder().id("0001").email("test@email.com").build());
        Optional<UserEntity> userEntity = mongoUserRepository.findUserByEmail("test@email.com");
        verify(mongoTemplate, times(1))
                .findOne(Query.query(Criteria.where("email").is("test@email.com")), UserEntity.class);
        assertTrue(userEntity.isPresent());
        assertEquals("0001", userEntity.get().getId());
        assertEquals("test@email.com", userEntity.get().getEmail());
    }
}