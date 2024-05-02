package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.entity.PreviousPlant;
import com.keb.kebsmartfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreviousPlantRepository extends JpaRepository<PreviousPlant, Long> {
    public List<PreviousPlant> findAllByUserSeqNum(long seqNum);
}
