package com.example.stamp_app.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Account {

    @Id
    @NotNull
    private UUID uuid;

    @NotNull
    @Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$")
    @Column(unique = true, name = "mailAddress")
    private String email;

    @Column
    @NotNull
    private String password;

    @Column
    private String name;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "account")
    private List<MicroController> microController;

}
