package org.khpi.diag.systems.lab1.model;

public enum Indication {
    LEUKOCYTES_NUMBER("Leukocytes"),
    LYMPHOCYTES_NUMBER("Lymphocytes"),
    T_LYMPHOCYTES("T-Lymphocytes"),
    T_HELPERS("T-Helpers"),
    REMANUFACTURED_T_SUPPRESSORS("RE-T-Suppressors"),
    SENS_THEOPHYLLINE("Sens. Theophylline"),
    RES_THEOPHYLLINE("Res. Theophylline"),
    B_LYMPHOCYTES("B-Lymphocytes");

    private final String shortIndicationName;

    Indication(String shortIndicationName) {
        this.shortIndicationName = shortIndicationName;
    }

    public String getShortIndicationName() {
        return shortIndicationName;
    }
}
