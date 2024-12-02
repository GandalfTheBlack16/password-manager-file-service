package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.model.entity.UserEntity;
import com.gandalftheblack.pm.fileservice.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MongoUserRepository implements UserRepository {

  private final MongoTemplate mongoTemplate;

  @Override
  public Optional<UserEntity> findUserByEmail(String email) {
    Query query = Query.query(Criteria.where("email").is(email));
    return Optional.ofNullable(mongoTemplate.findOne(query, UserEntity.class));
  }
}
