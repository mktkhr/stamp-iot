package com.example.stamp_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicroController {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NotNull
    @Column
    @Comment(value = "端末UUID")
    @Pattern(regexp = "^([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})$")
    private String uuid;

    @Column
    @Comment(value = "端末名")
    private String name;

    @NotNull
    @Comment(value = "MACアドレス")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$")
    private String macAddress;

    @Column
    @ColumnDefault("60")
    @Comment(value = "測定間隔(分)")
    @Pattern(regexp = "^(60|30|20|15|10|5|1)$")
    private String interval;

    @Column
    @Comment(value = "測定に使用するSDI-12アドレス(カンマ区切り)")
    @Pattern(regexp = "^(([0-9A-Za-z]{1},)*[0-9A-za-z]{1})|([0-9A-za-z]{1})$")
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
