//import org.bmc.service.IDataSource;
//import org.bmc.dto.PassengerInfoDTO;
//import org.bmc.model.PassengerInfo;
//import org.bmc.service.PassengerInfoServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class PassengerInfoServiceTest {
//
//    @InjectMocks
//    private PassengerInfoServiceImpl passengerInfoService;
//
//    @Mock
//    private IDataSource dataSource;
//
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetFareHistogram() throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        List<Double> fareData = Arrays.asList(10.0, 20.0, 30.0, 40.0); // Sample fare data
//        when(dataSource.getAllFares()).thenReturn(fareData);
//
//        passengerInfoService.getFareHistogramAsImage();
//
//        // Assert
//        // Add assertions to verify that the histogram is created or saved as expected.
//    }
//
//    @Test
//    public void testGetPassengerInfoByPassengerId() throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        Long passengerId = 123L;
//        PassengerInfo expectedPassenger = new PassengerInfo();
//        when(dataSource.findById(passengerId)).thenReturn(expectedPassenger);
//
//        PassengerInfoDTO result = passengerInfoService.getPassengerInfoByPassengerId(passengerId);
//
//        verify(dataSource, times(1)).findById(passengerId);
//        Assertions.assertEquals(expectedPassenger, result);
//    }
//
//    @Test
//    public void testGetAllPassengerInfo() throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<PassengerInfo> expectedPage = new PageImpl<>(Collections.emptyList());
//        when(dataSource.getAllPassengers(pageable)).thenReturn(expectedPage);
//
//        Page<PassengerInfoDTO> result = passengerInfoService.getAllPassengerInfo(pageable);
//
//        verify(dataSource, times(1)).getAllPassengers(pageable);
//        assertEquals(expectedPage, result);
//    }
//}