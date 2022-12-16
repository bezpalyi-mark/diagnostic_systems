package org.khpi.diag.systems.lab3.qualifier;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Diagnosis;
import org.khpi.diag.systems.lab3.model.Individual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiagnosisQualifier {

    private DiagnosisQualifier() {}

    public static Optional<Diagnosis> determineDiagnosis(Map<Diagnosis, List<Float>> hamingTable) {
        Map<Diagnosis, Double> gamingFinal = hamingTable.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .mapToDouble(Float::doubleValue)
                        .sum()));

        return gamingFinal.entrySet().stream()
                .filter(entry -> entry.getValue() == 0)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public static List<Diagnosis> findPossibleDiagnoses(Map<Diagnosis, List<Float>> hammingTable) {

        return hammingTable.entrySet().stream()
                .filter(entry -> !entry.getValue().contains(1f))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Map<Diagnosis, List<Float>> computeHammingTable(Individual individual, Map<Diagnosis, Map<Indication, Double>> medicalMemory) {
        Map<Diagnosis, List<Float>> hammingTable = new HashMap<>(medicalMemory.size());

        medicalMemory.forEach((diagnosis, indicationProbabilities) -> {
            hammingTable.put(diagnosis, new ArrayList<>());

            for (Map.Entry<Indication, Double> entry : indicationProbabilities.entrySet()) {
                Indication indication = entry.getKey();
                Double probability = entry.getValue();

                int x = individual.isPositiveDichotomy(indication) ? 1 : 0;
                hammingTable.get(diagnosis).add((float) Math.abs(x - probability));
            }
        });

        return hammingTable;
    }
}
