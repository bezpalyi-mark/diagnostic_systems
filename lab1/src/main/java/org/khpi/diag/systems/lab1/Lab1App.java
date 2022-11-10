package org.khpi.diag.systems.lab1;

import org.khpi.diag.systems.lab1.distance.Distances;
import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab1.normalization.Normalizations;
import org.khpi.diag.systems.lab1.reader.IndicationsFileReader;
import org.khpi.diag.systems.lab1.significance.DistributionCriteria;
import org.khpi.diag.systems.lab1.writer.ConsoleWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lab1App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final double p = 0.95;

        Map<Indication, List<Double>> indications = IndicationsFileReader.readFromFile("/lab1_v2.txt");
        int sizeN = indications.get(Indication.LEUKOCYTES_NUMBER).size();

        DistributionCriteria distributionCriteria = DistributionCriteria.builder()
                .sizeN(sizeN)
                .probability(p)
                .build();

        ConsoleWriter.printEDT(indications);

        //--------------------------------------------------------------------------------------------------------------
        // PEARSON
        //--------------------------------------------------------------------------------------------------------------

        List<List<Double>> pearsonRelationMatrix = Matrices.buildSymmetricRelations(indications, Coefficients::pearson);
        List<List<Integer>> testedPearson = Matrices.testSignificanceByDistribution(pearsonRelationMatrix, distributionCriteria::student);

        ConsoleWriter.printRelationMatrix("pearson", pearsonRelationMatrix);
        ConsoleWriter.printTestedRelationMatrix("pearson", sizeN, p, testedPearson);

        //--------------------------------------------------------------------------------------------------------------
        // KENDALL
        //--------------------------------------------------------------------------------------------------------------

        Map<Indication, List<Double>> indicationRanksMap = Matrices.calculateRanks(indications);
        List<List<Double>> kendallRelationMatrix = Matrices.buildSymmetricRelations(indicationRanksMap, Coefficients::kendall);
        List<List<Integer>> testedKendall = Matrices.testSignificanceByDistribution(kendallRelationMatrix, distributionCriteria::normal);

        ConsoleWriter.printRelationMatrix("kendall", kendallRelationMatrix);
        ConsoleWriter.printTestedRelationMatrix("kendall", sizeN, p, testedKendall);

        //--------------------------------------------------------------------------------------------------------------
        // CENTERING AND NORMALIZATION
        //--------------------------------------------------------------------------------------------------------------

        Map<Indication, List<Double>> normalizedIndications = indications.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Normalizations.unitCube(entry.getValue())));

        ConsoleWriter.printNormalizedEDT(normalizedIndications, "UNIT CUBE");

        //--------------------------------------------------------------------------------------------------------------
        // DISTANCE MATRIX
        //--------------------------------------------------------------------------------------------------------------

        List<List<Double>> distanceMatrix = Matrices.getDistanceMatrix(normalizedIndications, Distances::chebyshev);

        ConsoleWriter.printDistanceMatrix(distanceMatrix, "Chebyshev");
    }
}
