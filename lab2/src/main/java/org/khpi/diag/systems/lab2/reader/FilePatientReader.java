package org.khpi.diag.systems.lab2.reader;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab2.model.Factor;
import org.khpi.diag.systems.lab2.model.Patient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FilePatientReader {

    private FilePatientReader() {}

    public static List<Patient> fromFile(String filename) throws IOException, URISyntaxException {
        List<Patient> patients = new ArrayList<>();

        URL resource = FilePatientReader.class.getResource(filename);

        if (resource == null) {
            throw new FileNotFoundException("File " + filename + " was not found");
        }

        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path);

        lines.forEach(line -> {
            String[] parts = line.split("\t");

            if (parts.length < 6) {
                throw new IllegalStateException("File " + filename + " has unexpected data format");
            }

            Map<Factor, Double> factors = new EnumMap<>(Factor.class);
            factors.put(Factor.AGE, Double.parseDouble(parts[1]));
            factors.put(Factor.EXPERIENCE, Double.parseDouble(parts[2]));
            factors.put(Factor.MONTH_WORK_TIME, Double.parseDouble(parts[3]));

            Map.Entry<Indication, Double> response = Map.entry(Indication.T_LYMPHOCYTES, Double.parseDouble(parts[4]));

            int number = Integer.parseInt(parts[0]);
            int diagnosis = Integer.parseInt(parts[5]);

            Patient patient = new Patient(number, factors, response, diagnosis);
            patients.add(patient);
        });

        return patients;
    }
}
