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
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@EnableWebMvc
@RequestMapping("/passengers/info")
@Slf4j
@RequiredArgsConstructor
@Api(value = "Test Bmc Java Controller", description = "APIs" )
public class BmcTestJavaAppController {

    private final PassengerInfoServiceImpl passengerInfoService;
    private final HistogramService histogramService;

    @ApiOperation(value = "Given a PassengerId return all passenger data in Json format.")
    @GetMapping("/{passengerId}")
    @Async
    public CompletableFuture<ResponseEntity<PassengerInfoDTO>> getPassengerInfoByPassengerId(@PathVariable Long passengerId) {
        try {
            PassengerInfoDTO passengerInfo = passengerInfoService.getPassengerInfoByPassengerId(passengerId);
            return CompletableFuture.completedFuture(ResponseEntity.ok().body(passengerInfo));
        } catch (PassengerNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        } catch (IOException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            return CompletableFuture.completedFuture(ResponseEntity.unprocessableEntity().build());
        }
    }

    @ApiOperation(value = "Return a list of all passengers in Json format")
    @GetMapping()
    @Async
    public CompletableFuture<ResponseEntity<Page<PassengerInfoDTO>>> getPassengerInfoByPassengerId(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<PassengerInfoDTO> page = passengerInfoService.getAllPassengerInfo(pageable);
            return CompletableFuture.completedFuture(ResponseEntity.ok(page));
        } catch (IOException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            log.error(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }

    @ApiOperation(value = "Given a PassengerId and attribute list, return only requested attribute list from passenger data in Json format.")
    @GetMapping("/{passengerId}/attributes")
    @Async
    public CompletableFuture<ResponseEntity<PassengerInfoDTO>> getSpecificPassengerInfoByPassengerId(@PathVariable Long passengerId, @RequestParam List<String> attributes) {
        try {
            PassengerInfoDTO passengerInfo = passengerInfoService.getSpecificPassengerInfo(passengerId, attributes);
            return CompletableFuture.completedFuture(ResponseEntity.ok(passengerInfo));
        } catch (MissingAttributeException e) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        } catch (PassengerNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        } catch (IOException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            return CompletableFuture.completedFuture(ResponseEntity.unprocessableEntity().build());
        }
    }

    @ApiOperation(value = "Return a histogram (bar chart) of Fare prices in percentiles.")
    @GetMapping("/fare-histogram")
    @Async
    public CompletableFuture<ModelAndView> getFareHistogram() {
        HistogramData histogramData;
        try {
            histogramData = histogramService.calculateHistogram(20);
            ModelAndView modelAndView = new ModelAndView("histogram");
            modelAndView.addObject("histogramData", histogramData);
            return CompletableFuture.completedFuture(modelAndView);
        } catch (IOException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            ModelAndView modelAndView = new ModelAndView("error-view");
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            modelAndView.addObject("errorMessage", "An error occurred while getting all fares.");
            return CompletableFuture.completedFuture(modelAndView);
        }
    }

    // Optional
    @ApiOperation(value = "Return a histogram (bar chart) of Fare prices in percentiles.")
    @GetMapping("/fare-histogram/image")
    @Async
    public CompletableFuture<ResponseEntity<ByteArrayResource>> getFareHistogramImage() {
        try {
            passengerInfoService.getFareHistogramAsImage();

            Path path = Paths.get("histogram.png");
            byte[] imageBytes = Files.readAllBytes(path);

            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageBytes.length);
            headers.setContentDispositionFormData("attachment", "histogram.png");

            // Return the image as a ResponseEntity
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body(resource));
        } catch (IOException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        }
    }
}
