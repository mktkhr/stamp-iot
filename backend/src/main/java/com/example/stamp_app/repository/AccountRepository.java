package com.example.stamp_app.repository;

import com.example.stamp_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);

    Account findByUuid(UUID uuid);

    Account findById(BigInteger id);
}
