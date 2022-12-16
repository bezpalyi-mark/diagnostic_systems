package org.khpi.diag.systems.lab3;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Diagnosis;
import org.khpi.diag.systems.lab3.model.Individual;
import org.khpi.diag.systems.lab3.model.Stats;
import org.khpi.diag.systems.lab3.qualifier.DiagnosisQualifier;
import org.khpi.diag.systems.lab3.reader.FileIndividualsReader;
import org.khpi.diag.systems.lab3.statistics.StatisticsUtils;
import org.khpi.diag.systems.lab3.writer.ConsoleWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lab3App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        int[] indexes = {4, 50, 7, 3}; // specified by variant
        List<Individual> individuals = FileIndividualsReader.fromFile("/lab1_v2.txt");

        Stats stats = StatisticsUtils.collectStats(individuals); // N1, N2, N3 and other statistics with probabilities
        Map<Diagnosis, Map<Indication, Double>> medicalMemoryTable = StatisticsUtils.computeMedicalMemoryTable(stats);

        Map<Individual, List<Diagnosis>> determinationResults = new HashMap<>();

        for (int index : indexes) {
            Individual individual = individuals.get(index);

            Map<Diagnosis, List<Float>> hammingTable = DiagnosisQualifier.computeHammingTable(individual, medicalMemoryTable);

            List<Diagnosis> diagnoses = DiagnosisQualifier.determineDiagnosis(hammingTable)
                    .map(Collections::singletonList)
                    .orElseGet(() -> DiagnosisQualifier.findPossibleDiagnoses(hammingTable));

            determinationResults.put(individual, diagnoses);
        }

        ConsoleWriter.printDiagnosesStatistics(stats);
        ConsoleWriter.printIndicationStatistics(stats);
        ConsoleWriter.printDeterminationResults(determinationResults);
    }
}
