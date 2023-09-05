package org.bmc.dao;

import org.bmc.model.PassengerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPassengerInfoRepository extends JpaRepository<PassengerInfo, Long> {

    @Query(value = "SELECT p.fare FROM PassengerInfo p ORDER BY p.fare ASC")
    List<Double> findAllPassengerInfoFares();
}
