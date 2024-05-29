package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.dto.*;

import com.keb.kebsmartfarm.entity.PlantStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface KitAndPlantManageService {
    public ArduinoResponseDto addKit(ArduinoRequestDto requestDto);

    public void deleteKit(long kitNo);

    public PlantResponseDto plantingPlant(long kitNo, PlantRequestDto plantRequestDto);

    public PlantResponseDto completingPlantGrowth(long kitNo);

    public void deletingPlant(long kitNo);

    public Map<PlantStatus, List<PlantResponseDto>> gettingListOfUsersPlant();

    public boolean controlLight(long kitNo);

    public Map<String, LocalDateTime> supplyWater(long kitNo);

    public boolean validateKit(String serialNum);

    public Map<String, Object> getLatestDataList(long kitNo, String regDate);

    public List<PlantPictureResponseDto> loadAllPicsByPlantNum(long kitNo);

    public void savePicture(PlantPictureRequestDto requestDto);

    public Map<String, Object> loadPicture(String fileName);

}
