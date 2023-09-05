package org.bmc.configuration;

import lombok.Getter;
import org.bmc.model.PassengerInfo;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CsvWriter implements ItemWriter<PassengerInfo> {

    private final List<PassengerInfo> readData = new ArrayList<>();

    @Override
    public void write(List<? extends PassengerInfo> items) {
        readData.addAll(items);
    }

}
