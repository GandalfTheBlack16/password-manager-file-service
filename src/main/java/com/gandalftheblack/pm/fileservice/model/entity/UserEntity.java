package com.gandalftheblack.pm.fileservice.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Data
@Builder
public class UserEntity {
    @Id
    @Field("_id")
    private String id;
    private String email;
}
