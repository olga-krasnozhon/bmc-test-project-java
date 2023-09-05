package org.bmc.service;

import org.bmc.dto.PassengerInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PassengerInfoService {
    void getFareHistogramAsImage() throws IOException;

    PassengerInfoDTO getPassengerInfoByPassengerId(String passengerId) throws IOException;

    Page<PassengerInfoDTO> getAllPassengerInfo(Pageable pageable) throws IOException;

    PassengerInfoDTO getSpecificPassengerInfo(String passengerId, List<String> attributes) throws IOException;
}