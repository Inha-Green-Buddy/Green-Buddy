package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.dto.ArduinoRequestDto;
import com.keb.kebsmartfarm.dto.ArduinoResponseDto;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.ReleasedKit;
import com.keb.kebsmartfarm.repository.ArduinoKitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArduinoKitService {
    private final ArduinoKitRepository arduinoKitRepository;

    @Transactional
    public ArduinoResponseDto createArduinoKit(ArduinoRequestDto requestDto, ReleasedKit releasedKit) {
        if(isKitRegistered(requestDto.getSerialNum())){
            throw new IllegalStateException("이미 등록된 키트입니다.");
        }else{
            ArduinoKit arduinoKit = requestDto.toArduinoKit();
            arduinoKit.setReleasedKit(releasedKit);
            return ArduinoResponseDto.of(arduinoKitRepository.save(arduinoKit));
        }
    }

    /**
     * 시리얼 넘버로 아두이노 키트가 등록된 키트인지 확인하는 함수
     * @param serialNum
     * @return true -> 이미 등록된 키트, false -> 등록 가능한 키트
     */
    public boolean isKitRegistered(String serialNum) {
        return arduinoKitRepository.findBySerialNum(serialNum).isPresent();
    }

    @Transactional
    public void deleteKit(ArduinoKit arduinoKit) {
        // 관계를 끊습니다.
        ReleasedKit releasedKit = arduinoKit.getReleasedKit();
        releasedKit.setArduinoKit(null);
        arduinoKit.setReleasedKit(null);

        // 삭제를 수행합니다.
        arduinoKitRepository.delete(arduinoKit);
    }

    /**
     * releasedKit에서 kitNo으로 조회해 있으면 키트 번호를 없으면 IllegalStateException을 보내는 함수
     *
     * @param kitNo long 키트가 등록된 번호
     * @return 있으면 ArduinoKit, 없으면 IllegalStateException
     */
    public ArduinoKit findKitByKitNo(long kitNo) {
        return arduinoKitRepository.findById(kitNo)
                .orElseThrow(() -> new IllegalStateException("등록되지 않은 키트입니다."));
    }

    public List<ArduinoKit> getMyArduinoKits(long userSeqNum) {
        return arduinoKitRepository.findArduinoKitsByUserSeqNum(userSeqNum);
    }
}
