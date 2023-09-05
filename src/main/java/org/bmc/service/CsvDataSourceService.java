package org.bmc.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.bmc.model.PassengerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("csvDataSource")
public class CsvDataSourceService implements IDataSource {
    @Override
    public List<PassengerInfo> getData() throws IOException {
        return readCsvData("/src/main/resources/titanic.csv");
    }

    @Override
    public List<Double> getAllFares() throws IOException {
        List<PassengerInfo> passengers = getData();
        return passengers.stream().map(PassengerInfo::getFare).collect(Collectors.toList());
    }

    @Override
    public PassengerInfo findById(String passengerId) throws IOException {
        List<PassengerInfo> passengers = getData();
        return passengers.stream().filter(p -> Objects.equals(passengerId, p.getPassengerId())).findFirst().orElse(null);
    }

    @Override
    public Page<PassengerInfo> getAllPassengers(Pageable pageable) throws IOException {
        List<PassengerInfo> allPassengers = getData();
        return getPaginatedData(allPassengers, pageable.getPageNumber(), pageable.getPageSize());
    }

    public List<PassengerInfo> readCsvData(String csvFilePath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> csvData = reader.readAll();
            csvData.remove(0); // Remove the header row

            return csvData.stream()
                    .map(this::mapToPassengerInfo)
                    .collect(Collectors.toList());
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private PassengerInfo mapToPassengerInfo(String[] csvRow) {
        PassengerInfo passengerInfo = new PassengerInfo();
        passengerInfo.setPassengerId(Long.parseLong(csvRow[0]));
        passengerInfo.setSurvived(Integer.parseInt(csvRow[1]));
        passengerInfo.setPClass(Integer.parseInt(csvRow[2]));
        passengerInfo.setName(csvRow[3]);
        passengerInfo.setSex(csvRow[4]);
        passengerInfo.setAge(Double.parseDouble(csvRow[5]));
        passengerInfo.setSibSb(Integer.parseInt(csvRow[6]));
        passengerInfo.setParch(Integer.parseInt(csvRow[7]));
        passengerInfo.setTicket(csvRow[8]);
        passengerInfo.setFare(Double.parseDouble(csvRow[9]));
        passengerInfo.setCabin(csvRow[10]);
        passengerInfo.setEmbarked(csvRow[11]);
        return passengerInfo;
    }

    public Page<PassengerInfo> getPaginatedData(List<PassengerInfo> data, int pageNumber, int pageSize) {
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, data.size());

        if (fromIndex >= toIndex) {
            return Page.empty();
        }

        List<PassengerInfo> paginatedData = data.subList(fromIndex, toIndex);
        return new PageImpl<>(paginatedData, PageRequest.of(pageNumber, pageSize), data.size());
    }
}

