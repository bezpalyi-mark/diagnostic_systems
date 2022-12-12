package org.khpi.diag.systems.lab2;

import org.khpi.diag.systems.lab2.model.Factor;
import org.khpi.diag.systems.lab2.model.Patient;
import org.khpi.diag.systems.lab2.reader.FilePatientReader;
import org.khpi.diag.systems.lab2.writer.ConsoleWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lab2App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<Patient> patients = FilePatientReader.fromFile("/lab1_v2.txt");

        Map<Integer, List<Patient>> groupedByDiagnosis = patients.stream()
                .sorted(Comparator.comparing(Patient::getDiagnosis))
                .collect(Collectors.groupingBy(Patient::getDiagnosis));

        Map<Integer, List<List<Double>>> diagnosisToXMatrix = new HashMap<>(groupedByDiagnosis.size());
        Map<Integer, List<Double>> diagnosisToYMatrix = new HashMap<>(groupedByDiagnosis.size());
        Map<Integer, List<Double>> diagnosisToCoefficients = new HashMap<>();
        Map<Integer, Double> predictedVariableVariance = new HashMap<>();

        groupedByDiagnosis.forEach((diagnosis, patientList) -> {
            List<List<Double>> xMatrix = RegressionMatrices.calculateXMatrix(patientList);
            diagnosisToXMatrix.put(diagnosis, xMatrix);

            List<Double> yMatrix = RegressionMatrices.calculateYMatrix(patientList);
            diagnosisToYMatrix.put(diagnosis, yMatrix);

            List<Double> coefficients = RegressionMatrices.gaussianElimination(xMatrix, yMatrix);
            diagnosisToCoefficients.put(diagnosis, coefficients);

            Double squaresResidualSum = RegressionMatrices.squaresResidualSum(xMatrix, yMatrix, coefficients);
            double squareSigma = squaresResidualSum / (patients.size() - Factor.values().length - 1);
            predictedVariableVariance.put(diagnosis, squareSigma);
        });

        ConsoleWriter.printMatricesWithCoefficients(diagnosisToXMatrix, diagnosisToYMatrix, diagnosisToCoefficients,
                predictedVariableVariance);
    }
}
