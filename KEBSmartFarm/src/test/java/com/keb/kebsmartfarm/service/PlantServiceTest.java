package com.keb.kebsmartfarm.service;

import static org.junit.jupiter.api.Assertions.*;

import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    @Mock
    PlantRepository plantRepository;

    @InjectMocks
    PlantService plantService;

    @Test
    void 파일_없을_때_식물_생성() {
        ArduinoKit arduinoKit = ArduinoKit.builder()
                .kitNo(1L).build();

    }

    @Test
    void 식물_없는_상태에서_식물_삭제() {
        ArduinoKit arduinoKit = ArduinoKit.builder().build();
    }

}