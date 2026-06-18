package org.momtsim.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private static final String CSV_SEPARATOR = ",";

    public static List<String[]> read(String csvFile) {
        List<String[]> csvContent = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(ResourceUtils.open(csvFile), StandardCharsets.UTF_8))) {
            // Skip header
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                csvContent.add(line.split(CSV_SEPARATOR));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("error loading csv file: %s", csvFile), e);
        }
        return csvContent;
    }
}
