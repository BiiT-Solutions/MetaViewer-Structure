package com.biit.metaviewer.cadt;

public enum CadtVariables {

    RECEPTIVE("ReceptiveScore"),
    INNOVATOR("InnovatorScore"),
    STRATEGIST("StrategistScore"),
    VISIONARY("VisionaryScore"),
    LEADER("LeaderScore"),
    BANKER("BankerScore"),
    SCIENTIST("ScientistScore"),
    TRADESMAN("TradesmanScore"),

    ADAPTABILITY_ACTION("AdaptabilityActionScore"),
    STRUCTURE_INSPIRATION("StructureInspirationScore");

    private final String variable;


    CadtVariables(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}
