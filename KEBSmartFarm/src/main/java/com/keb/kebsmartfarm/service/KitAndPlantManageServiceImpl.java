package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.entity.PlantStatus;
import com.keb.kebsmartfarm.util.JsonUtil;
import com.keb.kebsmartfarm.config.MqttConfig;
import com.keb.kebsmartfarm.util.PictureUtils;
import com.keb.kebsmartfarm.util.SecurityUtil;
import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.*;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.ReleasedKit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KitAndPlantManageServiceImpl implements KitAndPlantManageService {

    private final ArduinoKitService arduinoKitService;
    private final ReleasedKitService releasedKitService;

    private final MqttConfig.MyGateway myGateway;
    private final PlantService plantService;
    private final String TOPIC;

    private final Map<String, String> COMMAND;

    private final MqttReceiver mqttReceiver;

    private final PlantWateringService plantWateringService;
    private final PlantPictureService plantPictureService;

    @Autowired
    public KitAndPlantManageServiceImpl(ArduinoKitService arduinoKitService,
                                        ReleasedKitService releasedKitService,
                                        @Qualifier("mqttConfig.MyGateway") MqttConfig.MyGateway myGateway,
                                        PlantService plantService,
                                        @Value("${Adafruit.username}") String username,
                                        MqttReceiver mqttReceiver,
                                        PlantWateringService plantWateringService,
                                        PlantPictureService plantPictureService) {
        this.arduinoKitService = arduinoKitService;
        this.releasedKitService = releasedKitService;
        this.myGateway = myGateway;
        this.plantService = plantService;
        this.TOPIC = String.format("%s/f/", username);
        this.plantPictureService = plantPictureService;
        this.COMMAND = new HashMap<>();
        COMMAND.put("command", "");
        this.mqttReceiver = mqttReceiver;
        this.plantWateringService = plantWateringService;
    }

    @Override
    @Transactional
    public boolean validateKit(String serialNum) {
        ReleasedKit releasedKit = releasedKitService.validateKitSerialNumber(serialNum)
                .orElseThrow(() -> new RuntimeException(Error.INVALID_SERIAL_NUMBER));
        return arduinoKitService.isKitRegistered(serialNum);
    }

    @Override
    @Transactional
    public ArduinoResponseDto addKit(ArduinoRequestDto requestDto) {
        ReleasedKit releasedKit = releasedKitService.validateKitSerialNumber(requestDto.getSerialNum())
                .orElseThrow(() -> new RuntimeException(Error.INVALID_SERIAL_NUMBER));
        // topic으로 등록 요청 고려
//        myGateway.sendToMqtt(JsonUtil.toJson(CommandDto.of("")));
        return arduinoKitService.createArduinoKit(requestDto, releasedKit);
    }

    @Override
    @Transactional
    public void deleteKit(long kitNo) {
        // Kit가 없으면 204 띄움
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        arduinoKitService.deleteKit(arduinoKit);
        // 지운 후 키트에 저장된 와이파이 정보 삭제
        COMMAND.replace("command", "delKit");
        myGateway.sendToMqtt(JsonUtil.toJson(COMMAND), arduinoKit.getSerialNum());

    }

    @Override
    @Transactional
    public PlantResponseDto plantingPlant(long kitNo, PlantRequestDto plantRequestDto) {
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        COMMAND.replace("command", "addPlant");
        myGateway.sendToMqtt(JsonUtil.toJson(COMMAND),
                TOPIC + arduinoKit.getSerialNum() + "-command");
        PlantResponseDto plant = plantService.createPlant(arduinoKit, plantRequestDto);
        plant.setProfileImg(PictureUtils.getUrl(plant.getStoredPath()));
        return plant;
    }

    @Override
    @Transactional
    public PlantResponseDto completingPlantGrowth(long kitNo) {
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        COMMAND.replace("command", "growth");
        myGateway.sendToMqtt(JsonUtil.toJson(COMMAND),
                TOPIC + arduinoKit.getSerialNum() + "-command");

        return plantService.cultivatePlant(arduinoKit);
    }

    @Override
    @Transactional
    public void deletingPlant(long kitNo) {
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        COMMAND.replace("command", "delPlant");
        myGateway.sendToMqtt(JsonUtil.toJson(COMMAND),
                TOPIC + arduinoKit.getSerialNum() + "-command");
        plantService.deleteActivePlant(arduinoKit);
    }

    @Override
    @Transactional
    public Map<PlantStatus, List<PlantResponseDto>> gettingListOfUsersPlant() {
        long seqNum = SecurityUtil.getCurrentUserId();
        return arduinoKitService.getMyArduinoKits(seqNum)
                .stream()
                .map(ArduinoKit::getPlantsByStatus)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }));
    }

    @Override
    public boolean controlLight(long kitNo) {
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        COMMAND.replace("command", "switch");
        myGateway.sendToMqtt(JsonUtil.toJson(COMMAND),
                TOPIC + arduinoKit.getSerialNum() + "-command");
        return true;
    }

    @Transactional
    public Map<String, Object> getLatestDataList(long kitNo, String regDate) {
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        Map<String, Object> DataList = new HashMap<>();
        DataList.put("sensorData", mqttReceiver.getLatestSensorData(kitNo, regDate));
        DataList.put("WateringDates", plantWateringService.findFiveLatestDates(arduinoKit));
        return DataList;
    }

    @Override
    @Transactional
    public Map<String, LocalDateTime> supplyWater(long kitNo) {
        Map<String, LocalDateTime> ret = new HashMap<>();
        ArduinoKit arduinoKit = arduinoKitService.findKitByKitNo(kitNo);
        COMMAND.replace("command", "water");
        myGateway.sendToMqtt(JsonUtil.toJson(COMMAND),
                TOPIC + arduinoKit.getSerialNum() + "-command");
        ret.put("date", plantWateringService.supplyWater(arduinoKit).getWateringDate());
        return ret;
    }

    @Override
    @Transactional
    public List<PlantPictureResponseDto> loadAllPicsByPlantNum(long plantNo) {
        List<PlantPictureResponseDto> plantPictureResponseDtoList = plantPictureService.loadAllByPlantNum(plantNo);
        plantPictureResponseDtoList.forEach(
                ppr -> ppr.setImageUrl(PictureUtils.getUrl(ppr.getStoredPath()))
        );
        return plantPictureResponseDtoList;
    }

    @Override
    public void savePicture(PlantPictureRequestDto requestDto) {
        plantPictureService.store(requestDto);
    }

    @Override
    public Map<String, Object> loadPicture(String fileName) {
        Map<String, Object> ret = new HashMap<>();
        MediaType mediaType = PictureUtils.getMediaTypeForExtension(PictureUtils.getFileExtension(fileName));
        ret.put("resource", plantPictureService.loadAsResource(fileName));
        ret.put("media", mediaType);
        return ret;
    }
}
