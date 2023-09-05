package org.bmc.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.bmc.model.HistogramData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class HistogramService {

    private final PassengerInfoServiceImpl passengerInfoService;

    public HistogramData calculateHistogram(int numBins) throws IOException {
        double[] fares = passengerInfoService.getAllFares();

        EmpiricalDistribution empiricalDistribution = new EmpiricalDistribution();
        empiricalDistribution.load(fares);

        // Calculate percentiles
        Percentile percentileCalculator = new Percentile();
        double[] percentiles = new double[numBins];
        for (int i = 0; i < numBins; i++) {
            double percentile = (i + 1) * 100.0 / numBins;
            double value = percentileCalculator.evaluate(fares, percentile);
            percentiles[i] = value;
        }

        // Calculate counts for each bin
        long[] counts = calculateBinCounts(fares, numBins);

        for (double value : fares) {
            int bin = calculateBinIndex(value, empiricalDistribution);

            // Ensure bin index is within bounds
            if (bin >= 0 && bin < counts.length) {
                counts[bin]++;
            }
        }

        return new HistogramData(percentiles, counts);
    }

    public long[] calculateBinCounts(double[] data, int numBins) {
        double min = Arrays.stream(data).min().orElse(0.0);
        double max = Arrays.stream(data).max().orElse(0.0);

        // Calculate bin width
        double binWidth = (max - min) / numBins;

        // Initialize bin counts
        long[] counts = new long[numBins];

        // Calculate counts for each bin
        for (double value : data) {
            int bin = (int) ((value - min) / binWidth);
            // Make sure the bin index is within the valid range
            bin = Math.max(0, Math.min(bin, numBins - 1));
            counts[bin]++;
        }

        return counts;
    }


    public int calculateBinIndex(double value, EmpiricalDistribution empiricalDistribution) {
        double[] upperBounds = empiricalDistribution.getUpperBounds();

        // Check if the value is outside the range of the bins
        if (value < upperBounds[0]) {
            return 0; // Value is less than the first bin
        } else if (value >= upperBounds[upperBounds.length - 1]) {
            return upperBounds.length - 1; // Value is greater than or equal to the last bin
        } else {
            // Iterate through the bins and find the appropriate bin index
            for (int i = 0; i < upperBounds.length; i++) {
                if (value <= upperBounds[i]) {
                    return i;
                }
            }
        }
        return -1;
    }
}