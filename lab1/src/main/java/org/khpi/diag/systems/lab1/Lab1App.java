package org.khpi.diag.systems.lab1;

import org.apache.commons.math.MathException;
import org.khpi.diag.systems.lab1.distribution.criteria.NormalDistributionCriteria;
import org.khpi.diag.systems.lab1.distribution.criteria.StudentDistributionCriteria;
import org.khpi.diag.systems.lab1.model.Indication;
import org.khpi.diag.systems.lab1.reader.IndicationsFileReader;
import org.khpi.diag.systems.lab1.writer.ConsoleWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class Lab1App {
    public static void main(String[] args) throws IOException, URISyntaxException, MathException {
        final double p = 0.95;

        //--------------------------------------------------------------------------------------------------------------
        // PEARSON
        //--------------------------------------------------------------------------------------------------------------

        Map<Indication, List<Double>> indications = IndicationsFileReader.readFromFile("/lab1_v2.txt");

        ConsoleWriter.printEDT(indications);

        List<List<Double>> pearsonRelationMatrix = Matrices.buildRelationMatrix(indications, Coefficients::pearson);

        ConsoleWriter.printRelationMatrix("pearson", pearsonRelationMatrix);

        int degreesOfFreedom = indications.get(Indication.LEUKOCYTES_NUMBER).size() - 2;

        StudentDistributionCriteria studentCriteria = new StudentDistributionCriteria(degreesOfFreedom);
        List<List<Integer>> testedPearson = studentCriteria.testRelationMatrix(pearsonRelationMatrix, p);

        ConsoleWriter.printTestedRelationMatrix("pearson", degreesOfFreedom, p, testedPearson);

        //--------------------------------------------------------------------------------------------------------------
        // KENDALL
        //--------------------------------------------------------------------------------------------------------------

        Map<Indication, List<Double>> indicationRanksMap = Matrices.calculateRanks(indications);
        List<List<Double>> kendallRelationMatrix = Matrices.buildRelationMatrix(indicationRanksMap, Coefficients::kendall);

        ConsoleWriter.printRelationMatrix("kendall", kendallRelationMatrix);

        NormalDistributionCriteria normalDistributionCriteria = new NormalDistributionCriteria();
        List<List<Integer>> testedKendall = normalDistributionCriteria.testRelationMatrix(kendallRelationMatrix, indications.get(Indication.LEUKOCYTES_NUMBER).size(), p);

        ConsoleWriter.printTestedRelationMatrix("kendall", 0, p, testedKendall);
    }
}
