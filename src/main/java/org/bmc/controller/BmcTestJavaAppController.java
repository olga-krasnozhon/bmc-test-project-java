package org.bmc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bmc.dto.PassengerInfoDTO;
import org.bmc.exception.MissingAttributeException;
import org.bmc.exception.PassengerNotFoundException;
import org.bmc.model.HistogramData;
import org.bmc.service.HistogramService;
import org.bmc.service.PassengerInfoServiceImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@EnableWebMvc
@RequestMapping("/passengers/info")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Test Bmc Java Controller")
public class BmcTestJavaAppController {

    private final PassengerInfoServiceImpl passengerInfoService;
    private final HistogramService histogramService;

    @ApiOperation(value = "Return a histogram (bar chart) of Fare prices in percentiles.")
    @GetMapping("/fare-histogram")
    public ModelAndView getFareHistogram() {
        HistogramData histogramData = null;
        try {
            histogramData = histogramService.calculateHistogram(20);
            ModelAndView modelAndView = new ModelAndView("histogram");
            modelAndView.addObject("histogramData", histogramData);
            return modelAndView;
        } catch (IOException e) {
            ModelAndView modelAndView = new ModelAndView("error-view");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            modelAndView.addObject("errorMessage", "An error occurred while getting all fares.");
            return modelAndView;
        }
    }

    @ApiOperation(value = "Return a histogram (bar chart) of Fare prices in percentiles.")
    @GetMapping("/fare-histogram/image")
    public ResponseEntity<ByteArrayResource> getFareHistogramImage() {
        try {
            passengerInfoService.getFareHistogramAsImage();

            Path path = Paths.get("histogram.png");
            byte[] imageBytes = Files.readAllBytes(path);

            // Create a ByteArrayResource to wrap the byte array
            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageBytes.length);
            headers.setContentDispositionFormData("attachment", "histogram.png");

            // Return the image as a ResponseEntity
            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Given a PassengerId return all passenger data in Json format.")
    @GetMapping("/passengerInfo/{passengerId}")
    public ResponseEntity<PassengerInfoDTO> getPassengerInfoByPassengerId(@PathVariable String passengerId) {
        try {
            PassengerInfoDTO passengerInfo = passengerInfoService.getPassengerInfoByPassengerId(passengerId);
            return ResponseEntity.ok().body(passengerInfo);
        } catch (PassengerNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @ApiOperation(value = "Return a list of all passengers in Json format")
    @GetMapping()
    public ResponseEntity<Page<PassengerInfoDTO>> getPassengerInfoByPassengerId(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<PassengerInfoDTO> page = passengerInfoService.getAllPassengerInfo(pageable);
            return ResponseEntity.ok(page);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Given a PassengerId and attribute list, return only requested attribute list from passenger data in Json format.")
    @GetMapping("/passengerInfo/{passengerId}/attributes")
    public ResponseEntity<PassengerInfoDTO> getSpecificPassengerInfoByPassengerId(@PathVariable String passengerId, @RequestParam List<String> attributes) {
        try {
            PassengerInfoDTO passengerInfo = passengerInfoService.getSpecificPassengerInfo(passengerId, attributes);
            return ResponseEntity.ok(passengerInfo);
        } catch (MissingAttributeException e) {
            return ResponseEntity.badRequest().build();
        } catch (PassengerNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
