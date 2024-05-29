package com.keb.kebsmartfarm.Controller;

import com.keb.kebsmartfarm.dto.*;
import com.keb.kebsmartfarm.entity.PlantStatus;
import com.keb.kebsmartfarm.service.KitAndPlantManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequestMapping("/kit")
@RestController
@RequiredArgsConstructor
public class KitController {

    private final KitAndPlantManageService kitAndPlantManageService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateKit(@RequestBody Map<String, String> req) {
        String serialNum = req.get("serialNum");
        return ResponseEntity.ok(kitAndPlantManageService.validateKit(serialNum));
    }

    @PostMapping
    public ResponseEntity<ArduinoResponseDto> addKit(@RequestBody ArduinoRequestDto requestDto) {
        return ResponseEntity.ok(kitAndPlantManageService.addKit(requestDto));
    }

    @DeleteMapping("/{kitNo}")
    public ResponseEntity<?> deleteKit(@PathVariable long kitNo) {
        kitAndPlantManageService.deleteKit(kitNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/{kitNo}/plant")
    public ResponseEntity<PlantResponseDto> addPlantToKit(@PathVariable long kitNo, @RequestParam("plantName") String name,
                                                          @RequestParam("plantNickName") String nickName,
                                                          @RequestParam("plantImage") MultipartFile file) {
        return ResponseEntity.ok(kitAndPlantManageService.plantingPlant(kitNo, PlantRequestDto.of(name, nickName, file)));
    }

    @PostMapping("/{kitNo}/growth")
    public ResponseEntity<PlantResponseDto> moveToPreviousPlant(@PathVariable long kitNo) {
        return ResponseEntity.ok(kitAndPlantManageService.completingPlantGrowth(kitNo));
    }

    @DeleteMapping("/{kitNo}/plant")
    public ResponseEntity<?> deletePlant(@PathVariable long kitNo) {
        kitAndPlantManageService.deletingPlant(kitNo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/plant/list")
    public ResponseEntity<Map<PlantStatus, List<PlantResponseDto>>> getUserPlantList() {
        return ResponseEntity.ok(kitAndPlantManageService.gettingListOfUsersPlant());
    }

    @GetMapping("/{kitNo}/light")
    public ResponseEntity<Boolean> lightKit(@PathVariable long kitNo) {
        return ResponseEntity.ok(kitAndPlantManageService.controlLight(kitNo));
    }

    @GetMapping("/{kitNo}/water")
    public ResponseEntity<Map<String, LocalDateTime>> supplyWaterToKit(@PathVariable long kitNo) {
        return ResponseEntity.ok(kitAndPlantManageService.supplyWater(kitNo));
    }

    @PostMapping("/plant/{plantNo}/picture")
    public ResponseEntity<?> addDiary(@PathVariable long plantNo,
                                      @RequestParam("file") MultipartFile file,
                                      @RequestParam("msg") String msg) {
        PlantPictureRequestDto requestDto = PlantPictureRequestDto.of(plantNo, file, msg);
        kitAndPlantManageService.savePicture(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/plant/{plantNo}/pictures")
    public ResponseEntity<List<PlantPictureResponseDto>> getListOfPicturesByPlantNum(@PathVariable long plantNo) {
        List<PlantPictureResponseDto> serveFile = kitAndPlantManageService.loadAllPicsByPlantNum(plantNo);
        return ResponseEntity.ok(serveFile);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Map<String, Object> ret = kitAndPlantManageService.loadPicture(filename);
        return ResponseEntity.ok()
                .contentType((MediaType) ret.get("media"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;").body((Resource) ret.get("resource"));
    }


    @GetMapping("/{kitNo}/details")
    public ResponseEntity<Map<String, Object>> getKitDetails(@PathVariable long kitNo, @RequestParam("regDate") String regDate) throws ParseException {
        // kitNo랑 regDate 받아오고
        // regDate 이후 데이터 중 최근 데이터 받아오기 orderBy desc -> 하나! + 최근 5번의 물 준 기록
        Map<String, Object> details = kitAndPlantManageService.getLatestDataList(kitNo, regDate);
        return ResponseEntity.ok(details);
    }
}
