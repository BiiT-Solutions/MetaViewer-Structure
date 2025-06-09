package com.biit.metaviewer.nca;

public enum NcaVariables {

    UNIVERSAL("universeel"),
    SOCIAL("maatschappelijk"),
    ADAPTABILITY("aanpassingsvermogen"),
    COMMUNICATION("communicatie"),
    ANALYSIS("analyse"),
    PROCESS_DESIGN("procesinrichting"),
    VISION("visie"),
    ACTION_ORIENTATION("actiegerichtheid"),
    STRENGTH("kracht"),
    RESPONSIBILITY("verantwoordelijkheid");

    private final String variable;


    NcaVariables(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}
