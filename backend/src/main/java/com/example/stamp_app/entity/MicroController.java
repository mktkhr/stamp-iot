package com.example.stamp_app.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
public class MicroController {

    @Id
    @NotNull
    @Comment(value = "Macアドレス")
    private String macAddress;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "microController")
    private List<MeasuredDataMaster> measuredDataMasters;

    @ManyToOne
    private Account account;
}
