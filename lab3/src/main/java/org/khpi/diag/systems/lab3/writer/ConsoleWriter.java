package org.khpi.diag.systems.lab3.writer;

import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab3.model.Diagnosis;
import org.khpi.diag.systems.lab3.model.Individual;
import org.khpi.diag.systems.lab3.model.Stats;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsoleWriter {

    private static final String SINGLE_SEPARATOR = "------------------------------------------------------------------";
    private static final String SEPARATOR_X6 = SINGLE_SEPARATOR + SINGLE_SEPARATOR + SINGLE_SEPARATOR
            + SINGLE_SEPARATOR + SINGLE_SEPARATOR + SINGLE_SEPARATOR;

    private ConsoleWriter() {
    }

    public static void printDiagnosesStatistics(Stats stats) {
        Map<Diagnosis, Double> diagnosisProbabilities = stats.getDiagnosisProbabilities();
        Map<Diagnosis, List<Individual>> individualsByDiagnosis = stats.getIndividualsByDiagnosis();
        Map<Diagnosis, Map<Indication, List<Individual>>> individualsIndications = stats.getIndividualsIndications();

        System.out.println("DIAGNOSIS PROBABILITIES: ");
        diagnosisProbabilities.forEach((diagnosis, probability) -> {
            System.out.printf("Diagnosis #%d | Probability %6.6s |%n", diagnosis.getNumber(), probability);
        });

        System.out.println(SEPARATOR_X6);

        System.out.println("(N1) GROUPED INDIVIDUALS (PATIENTS) BY DIAGNOSIS: ");
        individualsByDiagnosis.forEach((diagnosis, individuals) -> {
            List<Integer> individualsNumbers = individuals.stream()
                    .map(Individual::getNumber)
                    .collect(Collectors.toList());

            System.out.printf("Diagnosis #%d | %s%n", diagnosis.getNumber(), individualsNumbers);
        });

        System.out.println(SEPARATOR_X6);

        System.out.println("(N2) INDIVIDUALS (PATIENTS) INDICATIONS BY DIAGNOSES: ");
        individualsIndications.forEach((diagnosis, indicationsMap) -> {
            System.out.printf("Diagnosis #%d%n", diagnosis.getNumber());
            indicationsMap.forEach((indication, individuals) -> {
                List<Integer> individualsNumbers = individuals.stream()
                        .map(Individual::getNumber)
                        .collect(Collectors.toList());

                System.out.printf("Indication %-18s: %s%n", indication.getShortIndicationName(),
                        individualsNumbers);
            });
            System.out.println();
        });

        System.out.println(SEPARATOR_X6);

    }

    public static void printIndicationStatistics(Stats stats) {
        Map<Indication, List<Individual>> generalIndividualsIndications = stats.getGeneralIndividualsIndications();
        Map<Indication, Double> generalIndicationsProbabilities = stats.getGeneralIndicationsProbabilities();

        System.out.println("(N3) GROUPED INDIVIDUALS (PERSONS) BY INDICATION: ");
        generalIndividualsIndications.forEach((indication, individuals) -> {
            List<Integer> individualsNumbers = individuals.stream()
                    .map(Individual::getNumber)
                    .collect(Collectors.toList());

            System.out.printf("Indication %-18s: %s%n", indication.getShortIndicationName(),
                    individualsNumbers);
        });

        System.out.println(SEPARATOR_X6);

        System.out.println("GENERAL INDICATIONS PROBABILITIES");
        generalIndicationsProbabilities.forEach((indication, probability) -> {
            System.out.printf("Indication: %-18s | Probability: %6.6s%n", indication.getShortIndicationName(), probability);
        });

        System.out.println(SEPARATOR_X6);
    }

    public static void printDeterminationResults(Map<Individual, List<Diagnosis>> experimentalResults) {

        System.out.println("COMPUTED RESULTS VS ACTUAL RESULTS: ");

        experimentalResults.forEach((individual, diagnoses) -> {
            List<Integer> computedDiagnoses = diagnoses.stream()
                    .map(Diagnosis::getNumber)
                    .collect(Collectors.toList());

            System.out.printf("Patient #%-3d | COMPUTED: %-12s | ACTUAL: %d |%n", individual.getNumber(), computedDiagnoses,
                    individual.getDiagnosis().getNumber());
        });
    }
}
