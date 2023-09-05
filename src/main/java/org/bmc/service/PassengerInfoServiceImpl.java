package org.bmc.service;

import lombok.extern.slf4j.Slf4j;
import org.bmc.dto.PassengerInfoDTO;
import org.bmc.exception.MissingAttributeException;
import org.bmc.exception.PassengerNotFoundException;
import org.bmc.model.PassengerInfo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PassengerInfoServiceImpl implements PassengerInfoService {
    private final IDataSource dataSource;

    public PassengerInfoServiceImpl(
            @Value("${app.datasource}") String dataSourceType,
            IDataSource csvDataSource,
            IDataSource databaseDataSource
    ) {
        if ("csv".equals(dataSourceType)) {
            this.dataSource = csvDataSource;
        } else if ("database".equals(dataSourceType)) {
            this.dataSource = databaseDataSource;
        } else {
            throw new IllegalArgumentException("Invalid data source type");
        }
    }

    @Override
    public void getFareHistogramAsImage() throws IOException {
        List<Double> fareData = dataSource.getAllFares();

        double[] fares = fareData.stream().mapToDouble(Double::doubleValue).toArray();
        var dataset = new HistogramDataset();
        dataset.addSeries("key", fares, 30);

        JFreeChart histogram = ChartFactory.createHistogram("Fares Histogram",
                "fares", "percentiles", dataset);
        ChartUtils.saveChartAsPNG(new File("histogram.png"), histogram, 400, 300);
    }

    @Override
    public PassengerInfoDTO getPassengerInfoByPassengerId(String passengerId) throws PassengerNotFoundException, IOException {
        PassengerInfo passengerInfo = dataSource.findById(passengerId);
        if (passengerInfo == null) {
            log.error("Passenger not found.");
            throw new PassengerNotFoundException("Passenger with id: " + passengerId + " not found");
        }
        return PassengerInfoDTO.mapPassengerInfoToPassengerInfoDTO(passengerInfo);
    }

    @Override
    public Page<PassengerInfoDTO> getAllPassengerInfo(Pageable pageable) throws IOException {
        Page<PassengerInfo> passengerInfoPage = dataSource.getAllPassengers(pageable);
        return passengerInfoPage
                .stream()
                .map(PassengerInfoDTO::mapPassengerInfoToPassengerInfoDTO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> new PageImpl<>(list, pageable, passengerInfoPage.getTotalElements())
                ));
    }

    @Override
    public PassengerInfoDTO getSpecificPassengerInfo(String passengerId, List<String> attributes) throws MissingAttributeException, PassengerNotFoundException, IOException {
        PassengerInfo passenger = dataSource.findById(passengerId);
        if (passenger == null) {
            log.error("Passenger not found.");
            throw new PassengerNotFoundException("Passenger with id: " + passengerId + " not found");
        }

        validatePassedAttributes(passenger, attributes);
        return constructPassengerInfoDTO(passenger, attributes);
    }

    private void validatePassedAttributes(PassengerInfo passenger, List<String> attributes) {
        for (String attribute : attributes) {
            if (!attributeExists(passenger, attribute)) {
                log.error("Missing attribute has provided.");
                throw new MissingAttributeException("Attribute not found: " + attribute);
            }
        }
    }

    private boolean attributeExists(PassengerInfo passenger, String attributeName) {
        try {
            String getterMethodName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Method getterMethod = passenger.getClass().getMethod(getterMethodName);
            return getterMethod != null;
        } catch (NoSuchMethodException e) {
            log.error("The getter method does not exist for the specified attribute.");
            return false;
        }
    }

    private PassengerInfoDTO constructPassengerInfoDTO(PassengerInfo passenger, List<String> attributes) {
        PassengerInfoDTO passengerInfoDTO = new PassengerInfoDTO();
        passengerInfoDTO.setPassengerId(passenger.getPassengerId());

        for (String attribute : attributes) {
            switch (attribute.toUpperCase()) {
                case "SURVIVED":
                    passengerInfoDTO.setSurvived(passenger.getSurvived());
                    break;
                case "PCLASS":
                    passengerInfoDTO.setPClass(passenger.getPClass());
                    break;
                case "NAME":
                    passengerInfoDTO.setName(passenger.getName());
                    break;
                case "SEX":
                    passengerInfoDTO.setSex(passenger.getSex());
                    break;
                case "AGE":
                    passengerInfoDTO.setAge(passenger.getAge());
                    break;
                case "SIBSB":
                    passengerInfoDTO.setSibSb(passenger.getSibSb());
                    break;
                case "PARCH":
                    passengerInfoDTO.setParch(passenger.getParch());
                    break;
                case "TICKET":
                    passengerInfoDTO.setTicket(passenger.getTicket());
                    break;
                case "FARE":
                    passengerInfoDTO.setFare(passenger.getFare());
                    break;
                case "CABIN":
                    passengerInfoDTO.setCabin(passenger.getCabin());
                    break;
                case "EMBARKED":
                    passengerInfoDTO.setEmbarked(passenger.getEmbarked());
                    break;
            }
        }

        return passengerInfoDTO;
    }

    public double[] getAllFares() throws IOException {
        List<Double> fareData = dataSource.getAllFares();
        return fareData.stream().mapToDouble(Double::doubleValue).toArray();
    }
}