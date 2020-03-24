package com.vuvarov.rashod.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Model<ID extends Serializable> {

    public abstract ID getId();

    @CreatedDate
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY) //todo
    LocalDateTime insTime;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDateTime modifTime;
}

