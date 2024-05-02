package com.keb.kebsmartfarm.repository;

import com.keb.kebsmartfarm.entity.SensorData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    public List<SensorData> findByArduinoKitNoAndReceivedDateAfterOrderByReceivedDateDesc(Long arduinoKitNo, LocalDateTime receivedDate, PageRequest pageable);
}
