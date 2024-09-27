package com.keb.kebsmartfarm.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.PlantRequestDto;
import com.keb.kebsmartfarm.dto.PlantResponseDto;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.Plant;
import com.keb.kebsmartfarm.entity.PlantStatus;
import com.keb.kebsmartfarm.entity.Plants;
import com.keb.kebsmartfarm.repository.PlantRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;


@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    @Mock
    PlantRepository plantRepository;
    @Mock
    Plants plants;
    @InjectMocks
    PlantService plantService;
    ArduinoKit arduinoKit;

    @BeforeEach
    void init() {
        arduinoKit = ArduinoKit.builder()
                .kitNo(1L)
                .plants(plants)
                .build();
    }

    @Test
    void 식물_O_식물_생성_예외() {
        // given
        given(plants.hasGrowingPlant()).willReturn(true);
        PlantRequestDto request = new PlantRequestDto();

        // then
        assertThatThrownBy(() -> plantService.createPlant(arduinoKit, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(Error.KIT_ALREADY_HAVE_PLANT);
    }

    @Test
    void 식물_X_빈_파일_식물_생성_예외() throws IOException{
        // given
        given(plants.hasGrowingPlant()).willReturn(false);
        MockMultipartFile notExist = new MockMultipartFile(
                "file",
                "notexist.png",
                "image/png",
                new FileInputStream("src/test/resources/notexist.png")
        );
        PlantRequestDto request = PlantRequestDto.of("식물이름", "식물닉네임", notExist);

        // then
        assertThatThrownBy(() -> plantService.createPlant(arduinoKit, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(Error.CAN_NOT_STORE_EMPTY_FILE);

    }

    @Test
    void 식물_X_파일_O_이미지_X_식물_생성_예외() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "plantDefaultImage.txt",
                "text/plain",
                new FileInputStream("src/test/resources/test.txt")
        );

        PlantRequestDto requestDto = PlantRequestDto.of("식물이름", "식물닉네임", file);

        // then
        assertThatThrownBy(() -> plantService.createPlant(arduinoKit, requestDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(Error.NOT_IMAGE_FILE);
    }

    @Test
    @Transactional
    void 올바른_파일_식물_생성() throws IOException {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "plantDefaultImage.png",
                "image/png",
                new FileInputStream("src/test/resources/plantDefaultImg.png")
        );

        PlantRequestDto requestDto = PlantRequestDto.of("식물이름", "식물닉네임", image);
        Plant plant = Plant.builder()
                .plantName("식물이름")
                .plantNickName("식물닉네임")
                .storedFilePath("")
                .build();
        given(plantRepository.save(any(Plant.class))).willReturn(plant);

        // then
        assertDoesNotThrow(() -> plantService.createPlant(arduinoKit, requestDto));
    }

    @Test
    void 식물_삭제_키우는_식물_X() {
        // given
        ArduinoKit kitWithNoPlants = ArduinoKit.builder().build();
        // then
        assertThatThrownBy(() -> plantService.deleteActivePlant(kitWithNoPlants))
                .hasMessageContaining(Error.PLANT_NOT_REGISTERED)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 식물_삭제_정상() {
        // given
        Plant plant = Plant.builder()
                .plantNum(1L)
                .plantName("식물이름")
                .plantNickName("식물닉네임")
                .plantRegDate(LocalDateTime.now())
                .storedFilePath("")
                .build();
        given(plants.getGrowingPlant()).willReturn(plant);

        // then
        assertDoesNotThrow(() -> plantService.deleteActivePlant(arduinoKit));
    }

    @Test
    void 식물_성장_정상() {
        // given
        Plant plant = Plant.builder()
                .arduinoKit(arduinoKit)
                .plantNum(1L)
                .plantName("식물이름")
                .plantNickName("식물닉네임")
                .plantRegDate(LocalDateTime.now())
                .status(PlantStatus.GROWING)
                .storedFilePath("")
                .build();
        given(plants.getGrowingPlant()).willReturn(plant);
        given(plantRepository.save(any(Plant.class))).willReturn(plant);

        // when
        PlantResponseDto plantResponseDto = plantService.cultivatePlant(arduinoKit);

        // then
        assertThat(plantResponseDto.getArduinoKit()).isEqualTo(arduinoKit);
        assertThat(plantResponseDto.getStatus()).isEqualTo(PlantStatus.GROWN);
        assertThat(plantResponseDto.getPlantHarvestDate()).isNotNull();
    }



}