package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
