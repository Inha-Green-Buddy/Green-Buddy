package com.keb.kebsmartfarm.service;

import com.keb.kebsmartfarm.util.PictureUtils;
import com.keb.kebsmartfarm.constant.Message.Error;
import com.keb.kebsmartfarm.dto.PlantPictureRequestDto;
import com.keb.kebsmartfarm.dto.PlantPictureResponseDto;
import com.keb.kebsmartfarm.repository.PlantPictureRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.keb.kebsmartfarm.util.PictureUtils.rootLocation;

@Slf4j
@Service
public class PlantPictureServiceImpl implements PlantPictureService {

    private final PlantPictureRepository plantPictureRepository;

    @Autowired
    public PlantPictureServiceImpl(PlantPictureRepository plantPictureRepository) {
        this.plantPictureRepository = plantPictureRepository;
    }


    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void store(PlantPictureRequestDto plantPictureDto) {
        MultipartFile file = plantPictureDto.getFile();
        Path destinationFile = PictureUtils.getDestPath(file);
        try {
            file.transferTo(destinationFile);
            plantPictureRepository.save(plantPictureDto.toPlantPicture(destinationFile));
        } catch (IOException e) {
            throw new IllegalStateException(Error.FAILED_TO_SAVE_FILE, e);
        }
    }

    @Override
    @Transactional
    public List<PlantPictureResponseDto> loadAllByPlantNum(Long plantNum) {
        // 필요한 거 -> Path, date, msg
        return this.plantPictureRepository.findAllByPlantNumOrderByDateDesc(plantNum)
                .stream().map(PlantPictureResponseDto::of).toList();
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException(String.format(Error.CAN_NOT_READ_FILE ,filename));
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format(Error.CAN_NOT_READ_FILE, (filename + e)));
        }
    }
}
