package org.khpi.diag.systems.lab1.reader;

import lombok.experimental.UtilityClass;
import org.khpi.diag.systems.lab1.model.Indication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class IndicationsFileReader {

    public static Map<Indication, List<Double>> readFromFile(String fileName) throws IOException, URISyntaxException {
        Map<Indication, List<Double>> result = new HashMap<>();

        Arrays.stream(Indication.values()).forEach(indication -> result.put(indication, new ArrayList<>()));

        URL resource = IndicationsFileReader.class.getResource(fileName);

        if (resource == null) {
            throw new FileNotFoundException("File " + fileName + " was not found");
        }

        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path);

        lines.forEach(line -> {
            String[] parts = line.split("\t");

            if (parts.length < 10) {
                throw new IllegalStateException("File " + fileName + " has unexpected data format");
            }

            double leukocytesNumber = Double.parseDouble(parts[1]);
            double lymphocytesNumber = Double.parseDouble(parts[2]);
            double tLymphocytes = Double.parseDouble(parts[3]);
            double tHelpers = Double.parseDouble(parts[4]);
            double remanufacturedTSuppressors = Double.parseDouble(parts[5]);
            double sensitiveTheophylline = Double.parseDouble(parts[6]);
            double resistantTheophylline = Double.parseDouble(parts[7]);
            double bLymphocytes = Double.parseDouble(parts[8]);

            result.get(Indication.LEUKOCYTES_NUMBER).add(leukocytesNumber);
            result.get(Indication.LYMPHOCYTES_NUMBER).add(lymphocytesNumber);
            result.get(Indication.T_LYMPHOCYTES).add(tLymphocytes);
            result.get(Indication.T_HELPERS).add(tHelpers);
            result.get(Indication.REMANUFACTURED_T_SUPPRESSORS).add(remanufacturedTSuppressors);
            result.get(Indication.SENS_THEOPHYLLINE).add(sensitiveTheophylline);
            result.get(Indication.RES_THEOPHYLLINE).add(resistantTheophylline);
            result.get(Indication.B_LYMPHOCYTES).add(bLymphocytes);
        });

        return result;
    }
}
