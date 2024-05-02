package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.config.JsonUtil;
import com.keb.kebsmartfarm.dto.SensorDataDto;
import com.keb.kebsmartfarm.entity.SensorData;
import com.keb.kebsmartfarm.repository.SensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// 메시지 받아 처리할 클래스 지정
@MessageEndpoint
@RequiredArgsConstructor
public class MqttReceiver {

    private final SensorDataRepository sensorDataRepository;

    private final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            // msec를 9자리까지 설정
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .toFormatter();



    @Transactional
    public void handle(Message<?> msg) {
        String topic = (String) msg.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        System.out.println("Topic : " + topic);
        System.out.println("Payload : " + msg.getPayload());
        saveSensorData(JsonUtil.fromJson((String) msg.getPayload(), SensorDataDto.class).toSensorData());
    }


    public void saveSensorData(SensorData sensorData) {
        this.sensorDataRepository.save(sensorData);
    }

    @Transactional
    public List<SensorDataDto> getLatestSensorData(long kitNo, String date) {
        return sensorDataRepository.
                findByArduinoKitNoAndReceivedDateAfterOrderByReceivedDateDesc(
                        kitNo, LocalDateTime.parse(date, formatter),
                        PageRequest.of(0, 1)
                ).stream().map(SensorDataDto::of).toList();
    }
}
