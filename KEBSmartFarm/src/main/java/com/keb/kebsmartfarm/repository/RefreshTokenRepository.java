package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.jwt.RefreshToken;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    List<RefreshToken> findAllByMemberId(Long memberId);
}
