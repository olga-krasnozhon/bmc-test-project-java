package org.bmc.service;

import org.bmc.dao.IPassengerInfoRepository;
import org.bmc.model.PassengerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("databaseDataSource")
public class DatabaseDataSource implements IDataSource {

    private IPassengerInfoRepository passengerInfoJpaRepository;

    public DatabaseDataSource(IPassengerInfoRepository passengerInfoJpaRepository) {
        this.passengerInfoJpaRepository = passengerInfoJpaRepository;
    }

    @Override
    public IPassengerInfoRepository getData() {
        // Return the database repository for custom queries
        return passengerInfoJpaRepository;
    }

    @Override
    public List<Double> getAllFares() {
        return passengerInfoJpaRepository.findAllPassengerInfoFares();
    }

    @Override
    public PassengerInfo findById(Long passengerId) {
        Optional<PassengerInfo> passengerInfo = passengerInfoJpaRepository.findById(String.valueOf(passengerId));
        return passengerInfo.orElse(null);
    }

    @Override
    public Page<PassengerInfo> getAllPassengers(Pageable pageable) {
        return passengerInfoJpaRepository.findAll(pageable);
    }
}

