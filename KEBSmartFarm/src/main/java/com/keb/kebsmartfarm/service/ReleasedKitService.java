package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.entity.ReleasedKit;
import com.keb.kebsmartfarm.repository.ReleasedKitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReleasedKitService {
    private final ReleasedKitRepository releasedKitRepository;

    public Optional<ReleasedKit> validateKitSerialNumber(String serialNum) {
        return releasedKitRepository.findByKitSerialNum(serialNum);
    }
}
