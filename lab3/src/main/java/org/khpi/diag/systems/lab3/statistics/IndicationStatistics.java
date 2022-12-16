package org.khpi.diag.systems.lab3.statistics;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Individual;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IndicationStatistics {

    private IndicationStatistics() {
    }

    public static Map<Indication, Double> generalProbabilities(List<Individual> individuals) {
        return Arrays.stream(Indication.values())
                .collect(Collectors.toMap(Function.identity(), indication -> {
                    List<Individual> individualsWithPositiveDichotomy = individuals.stream()
                            .filter(individual -> individual.isPositiveDichotomy(indication))
                            .collect(Collectors.toList());

                    return individualsWithPositiveDichotomy.isEmpty()
                            ? 0
                            : (double) individualsWithPositiveDichotomy.size() / individuals.size();
                }));
    }

    public static Map<Indication, List<Individual>> individualsWithIndications(List<Individual> individuals) {
        Map<Indication, List<Individual>> result = new EnumMap<>(Indication.class);

        Arrays.stream(Indication.values())
                .forEach(indication -> {
                    List<Individual> individualsWithPositiveDichotomy = individuals.stream()
                            .filter(individual -> individual.isPositiveDichotomy(indication))
                            .collect(Collectors.toList());

                    result.put(indication, individualsWithPositiveDichotomy);
                });

        return result;
    }
}
