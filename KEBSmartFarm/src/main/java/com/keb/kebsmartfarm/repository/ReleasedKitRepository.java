package com.keb.kebsmartfarm.repository;


import com.keb.kebsmartfarm.entity.ReleasedKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReleasedKitRepository extends JpaRepository<ReleasedKit, Long> {
    public Optional<ReleasedKit> findByKitSerialNum(String serialNum);
}
