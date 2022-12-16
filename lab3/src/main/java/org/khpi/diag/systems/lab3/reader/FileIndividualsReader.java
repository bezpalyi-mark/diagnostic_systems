package org.khpi.diag.systems.lab3.reader;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Diagnosis;
import org.khpi.diag.systems.lab3.model.IndicationsNorms;
import org.khpi.diag.systems.lab3.model.Individual;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileIndividualsReader {

    private FileIndividualsReader() {
    }

    public static List<Individual> fromFile(String filename) throws IOException, URISyntaxException {
        List<Individual> result = new ArrayList<>();

        URL resource = FileIndividualsReader.class.getResource(filename);

        if (resource == null) {
            throw new FileNotFoundException("File " + filename + " was not found");
        }

        Path path = Paths.get(resource.toURI());
        List<String> lines = Files.readAllLines(path);

        lines.forEach(line -> {
            String[] parts = line.split("\t");

            if (parts.length < 10) {
                throw new IllegalStateException("File " + filename + " has unexpected data format");
            }

            int individualNumber = Integer.parseInt(parts[0]);
            int diagnosisNumber = Integer.parseInt(parts[9]);

            Diagnosis diagnosis = new Diagnosis(diagnosisNumber);
            Individual individual = new Individual(individualNumber, diagnosis, new IndicationsNorms());

            individual.addIndication(Indication.LEUKOCYTES_NUMBER, Double.parseDouble(parts[1]));
            individual.addIndication(Indication.LYMPHOCYTES_NUMBER, Double.parseDouble(parts[2]));
            individual.addIndication(Indication.T_LYMPHOCYTES, Double.parseDouble(parts[3]));
            individual.addIndication(Indication.T_HELPERS, Double.parseDouble(parts[4]));
            individual.addIndication(Indication.REMANUFACTURED_T_SUPPRESSORS, Double.parseDouble(parts[5]));
            individual.addIndication(Indication.SENS_THEOPHYLLINE, Double.parseDouble(parts[6]));
            individual.addIndication(Indication.RES_THEOPHYLLINE, Double.parseDouble(parts[7]));
            individual.addIndication(Indication.B_LYMPHOCYTES, Double.parseDouble(parts[8]));

            result.add(individual);
        });

        return result;
    }
}
