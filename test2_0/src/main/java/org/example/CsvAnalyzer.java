package org.example;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CsvAnalyzer {

    private final Map<String, Integer> duplicatesCsv = new HashMap<>();
    private final Map<String, BigDecimal> totalWeightPerGroup = new HashMap<>();
    private long maxWeightCsv = Long.MIN_VALUE;
    private long minWeightCsv = Long.MAX_VALUE;

    public void analyzeCsvFile(String filePath) {
        clearDataCsv();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.skip(1)
                    .forEach(this::processCsvLine);
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    private void processCsvLine(String line) {
        String[] data = line.split(",");
        String group = data[0];
        String type = data[1];
        long weight = Long.parseLong(data[3]);

        maxWeightCsv = Math.max(maxWeightCsv, weight);
        minWeightCsv = Math.min(minWeightCsv, weight);
        String key = group + type;
        duplicatesCsv.put(key, duplicatesCsv.getOrDefault(key, 0) + 1);

        totalWeightPerGroup.put(group, totalWeightPerGroup.getOrDefault(group, BigDecimal.ZERO).add(BigDecimal.valueOf(weight)));
    }

    public Map<String, Integer> getDuplicatesCsv() {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : duplicatesCsv.entrySet()) {
            if (entry.getValue() >= 2) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public Map<String, BigDecimal> getTotalWeightPerGroupCsv() {
        return totalWeightPerGroup;
    }

    public long getMaxWeightCsv() {
        return maxWeightCsv;
    }

    public long getMinWeightCsv() {
        return minWeightCsv;
    }

    private void clearDataCsv() {
        duplicatesCsv.clear();
        totalWeightPerGroup.clear();
        maxWeightCsv = Long.MIN_VALUE;
        minWeightCsv = Long.MAX_VALUE;
    }
}