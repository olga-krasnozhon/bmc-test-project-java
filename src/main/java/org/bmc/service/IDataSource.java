package org.bmc.service;

import org.bmc.model.PassengerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IDataSource {
    Object getData() throws IOException;

    List<Double> getAllFares() throws IOException;

    PassengerInfo findById(String passengerId) throws IOException;

    Page<PassengerInfo> getAllPassengers(Pageable pageable) throws IOException;
}
