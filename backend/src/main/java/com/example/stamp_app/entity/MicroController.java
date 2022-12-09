package com.example.stamp_app.entity;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
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
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$")
    private String macAddress;

    @NotNull
    @Column
    @ColumnDefault("60")
    @Comment(value = "測定間隔(分)")
    private int interval;

    @Column
    @Comment(value = "測定に使用するSDI-12アドレス(カンマ区切り)")
    @Pattern(regexp = "^([0-9A-Za-z]{1},)*[0-9A-za-z]{1}$")
    private String sdi12Address;

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
