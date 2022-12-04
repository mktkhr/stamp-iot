package com.example.stamp_app.entity;

import lombok.Data;
import org.hibernate.annotations.Comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
public class MicroController {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

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
