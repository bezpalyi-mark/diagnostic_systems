package org.khpi.diag.systems.lab3.model;

import org.khpi.diag.systems.lab1.model.Indication;

import java.util.Map;

public final class IndicationsNorms implements Norms<Indication> {
    private final Map<Indication, Range> indicationRangeMap = Map.of(
            Indication.LEUKOCYTES_NUMBER, new Range(Integer.MIN_VALUE, 5),
            Indication.LYMPHOCYTES_NUMBER, new Range(1.5f, 2.5f),
            Indication.T_LYMPHOCYTES, new Range(1, 2),
            Indication.T_HELPERS, new Range(0.6f, 0.8f),
            Indication.REMANUFACTURED_T_SUPPRESSORS, new Range(0.3f, 0.5f),
            Indication.SENS_THEOPHYLLINE, new Range(0.1f, 0.2f),
            Indication.RES_THEOPHYLLINE, new Range(0.8f, Integer.MAX_VALUE),
            Indication.B_LYMPHOCYTES, new Range(0.15f, 0.3f)
    );

    public boolean test(Indication indication, float indicationValue) {
        Range indicationRange = indicationRangeMap.get(indication);

        if (indicationRange == null) {
            throw new IllegalArgumentException("Indication " + indication.getShortIndicationName() + " not supported");
        }

        return indicationRange.inRange(indicationValue);
    }

    private static class Range {
        private final float begin;
        private final float end;

        public Range(float begin, float end) {
            this.begin = begin;
            this.end = end;
        }

        private boolean inRange(float indicationValue) {
            return indicationValue > begin && indicationValue < end;
        }
    }
}
