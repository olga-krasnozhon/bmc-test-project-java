package org.bmc.service;

import org.bmc.dto.PassengerInfoDTO;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PassengerInfoService {
    void getFareHistogramAsImage() throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

    PassengerInfoDTO getPassengerInfoByPassengerId(Long passengerId) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

    Page<PassengerInfoDTO> getAllPassengerInfo(Pageable pageable) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

    PassengerInfoDTO getSpecificPassengerInfo(Long passengerId, List<String> attributes) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;
}