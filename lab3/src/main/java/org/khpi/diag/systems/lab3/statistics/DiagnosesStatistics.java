package org.khpi.diag.systems.lab3.statistics;

import org.apache.commons.collections4.CollectionUtils;
import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Diagnosis;
import org.khpi.diag.systems.lab3.model.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagnosesStatistics {

    private DiagnosesStatistics() {
    }

    public static Map<Diagnosis, Double> priorProbabilities(Map<Diagnosis, List<Individual>> individualsByDiagnosis) {
        Map<Diagnosis, Double> result = new HashMap<>(individualsByDiagnosis.size());

        int size = individualsByDiagnosis.values().stream()
                .map(Collection::size)
                .mapToInt(Integer::intValue)
                .sum();

        individualsByDiagnosis.forEach((diagnosis, individuals) -> {
            double probability = (double) individuals.size() / size;
            result.put(diagnosis, probability);
        });

        return result;
    }

    public static Map<Diagnosis, Map<Indication, List<Individual>>> individualsIndications(Map<Diagnosis, List<Individual>> individualsByDiagnosis,
                                                                                           Map<Indication, List<Individual>> individualsWithIndications) {
        Map<Diagnosis, Map<Indication, List<Individual>>> result = new HashMap<>();
        individualsByDiagnosis.keySet().forEach(diagnosis -> result.put(diagnosis, new EnumMap<>(Indication.class)));

        individualsByDiagnosis.forEach((diagnosis, individualsWithDiagnosis) ->
                Arrays.stream(Indication.values()).forEach(indication -> {
                    List<Individual> individualsWithIndication = individualsWithIndications.get(indication);
                    Collection<Individual> intersection = CollectionUtils.intersection(individualsWithDiagnosis, individualsWithIndication);
                    result.get(diagnosis).put(indication, new ArrayList<>(intersection));
                })
        );

        return result;
    }

}
