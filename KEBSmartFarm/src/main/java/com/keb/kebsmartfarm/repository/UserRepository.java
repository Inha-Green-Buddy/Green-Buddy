package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByUserEmail(String userEmail);
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);
}
