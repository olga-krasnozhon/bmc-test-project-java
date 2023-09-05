package org.bmc.service;

import org.bmc.configuration.CsvWriter;
import org.bmc.model.PassengerInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("csvDataSource")
public class CsvDataSourceService implements IDataSource {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("readCSVFileJob")
    private Job csvJob;

    @Autowired
    private CsvWriter csvWriter;

    @Override
    public List<PassengerInfo> getData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        return readCsvData();
    }

    @Override
    public List<Double> getAllFares() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        List<PassengerInfo> passengers = getData();
        return passengers.stream().map(PassengerInfo::getFare).collect(Collectors.toList());
    }

    @Override
    public PassengerInfo findById(Long passengerId) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        List<PassengerInfo> passengers = getData();
        return passengers.stream().filter(p -> Objects.equals(passengerId, p.getPassengerId())).findFirst().orElse(null);
    }

    @Override
    public Page<PassengerInfo> getAllPassengers(Pageable pageable) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        List<PassengerInfo> allPassengers = getData();
        return getPaginatedData(allPassengers, pageable.getPageNumber(), pageable.getPageSize());
    }

    public List<PassengerInfo> readCsvData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        if (csvWriter.getReadData().isEmpty()) {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(csvJob, jobParameters);
        }
        return csvWriter.getReadData();
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

