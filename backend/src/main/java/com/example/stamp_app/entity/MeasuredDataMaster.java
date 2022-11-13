package com.example.stamp_app.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class MeasuredDataMaster {

    @Id
    @NotNull
    private UUID uuid;

    @Column
    private String dayOfYear;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @ManyToOne
    private MicroController microController;

    @OneToMany(mappedBy = "measuredDataMaster")
    private List<Sdi12Data> sdi12Data;

    @OneToMany(mappedBy = "measuredDataMaster")
    private List<EnvironmentalData> environmentalData;

    @Column
    private String voltage;

}
