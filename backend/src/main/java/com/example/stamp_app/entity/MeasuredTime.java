package com.example.stamp_app.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeasuredTime {

    @Id
    @NotNull
    private UUID ulid;

    @ManyToOne
    private MicroController microController;

    @Column
    @NotNull
    private LocalDateTime measuringTime;

    @Column
    private Integer dayOfYear;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "measuredTime")
    private List<MeasuredData> measuredData;
}
