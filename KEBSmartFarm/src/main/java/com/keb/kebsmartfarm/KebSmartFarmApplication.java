package com.keb.kebsmartfarm;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
public class KebSmartFarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(KebSmartFarmApplication.class, args);
    }

    @PostConstruct
    public void setTimeZone(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul" ));
        LocalDateTime time = LocalDateTime.now();
        System.out.println("time = " + time);
    }
}
