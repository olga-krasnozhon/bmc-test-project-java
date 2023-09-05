package org.bmc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.bmc.model.PassengerInfo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PassengerInfoLogProcessor implements ItemProcessor<PassengerInfo, PassengerInfo> {

    public PassengerInfo process(PassengerInfo passengerInfo) {
        log.info("Inserting passengerInfo : " + passengerInfo);
        return passengerInfo;
    }
}
