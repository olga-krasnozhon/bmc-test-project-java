package org.bmc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistogramData {
    private final double[] percentiles;
    private final long[] counts;
}
