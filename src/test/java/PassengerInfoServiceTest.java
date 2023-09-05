import org.bmc.service.IDataSource;
import org.bmc.dto.PassengerInfoDTO;
import org.bmc.model.PassengerInfo;
import org.bmc.service.PassengerInfoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerInfoServiceTest {

    @InjectMocks
    private PassengerInfoServiceImpl passengerInfoService;

    @Mock
    private IDataSource dataSource;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFareHistogram() throws IOException {
        // Arrange
        List<Double> fareData = Arrays.asList(10.0, 20.0, 30.0, 40.0); // Sample fare data
        when(dataSource.getAllFares()).thenReturn(fareData);

        // Act
        passengerInfoService.getFareHistogramAsImage();

        // Assert
        // Add assertions to verify that the histogram is created or saved as expected.
    }

    @Test
    public void testGetPassengerInfoByPassengerId() throws IOException {
        // Arrange
        String passengerId = "123"; // Sample passenger ID
        PassengerInfo expectedPassenger = new PassengerInfo();
        when(dataSource.findById(passengerId)).thenReturn(expectedPassenger);

        // Act
        PassengerInfoDTO result = passengerInfoService.getPassengerInfoByPassengerId(passengerId);

        // Assert
        verify(dataSource, times(1)).findById(passengerId);
        Assertions.assertEquals(expectedPassenger, result);
    }

    @Test
    public void testGetAllPassengerInfo() throws IOException {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<PassengerInfo> expectedPage = new PageImpl<>(Collections.emptyList());
        when(dataSource.getAllPassengers(pageable)).thenReturn(expectedPage);

        // Act
        Page<PassengerInfoDTO> result = passengerInfoService.getAllPassengerInfo(pageable);

        // Assert
        verify(dataSource, times(1)).getAllPassengers(pageable);
        assertEquals(expectedPage, result);
    }
}