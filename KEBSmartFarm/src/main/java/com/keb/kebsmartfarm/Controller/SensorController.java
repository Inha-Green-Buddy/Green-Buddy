package com.keb.kebsmartfarm.Controller;

import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.entity.ArduinoKit;
import com.keb.kebsmartfarm.entity.ReleasedKit;
import com.keb.kebsmartfarm.service.ArduinoKitService;
import com.keb.kebsmartfarm.service.ReleasedKitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sensor")
@RestController
@RequiredArgsConstructor
public class SensorController {

    private final ReleasedKitService releasedKitService;
    private final ArduinoKitService arduinoKitService;

    @PostMapping("/certificate")
    public ResponseEntity<Long> certificateKit(@RequestBody String serialNumber) {
        ReleasedKit releasedKit = releasedKitService.validateKitSerialNumber(serialNumber)
                .orElseThrow(() -> new IllegalStateException(Error.KIT_NOT_EXIST));
        return ResponseEntity.ok(releasedKit.getReleaseNum());
    }

    @GetMapping("/{kitNo}/plant")
    public ResponseEntity<Boolean> hasPlant(@PathVariable long kitNo){
        return ResponseEntity.ok(arduinoKitService.findKitByKitNo(kitNo).getActivePlant().isPresent());
    }
}
