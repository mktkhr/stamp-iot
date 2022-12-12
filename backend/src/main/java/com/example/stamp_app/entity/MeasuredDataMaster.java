package com.example.stamp_app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class MeasuredDataMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "measuredDataMaster")
    private List<Sdi12Data> sdi12Data;

    @JsonManagedReference
    @OneToMany(mappedBy = "measuredDataMaster")
    private List<EnvironmentalData> environmentalData;

    @Column
    private String voltage;

}
