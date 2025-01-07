package com.biit.metaviewer.cadt;

public enum CadtArchetypes {
    RECEPTIVE("receptive"),
    INNOVATOR("innovator"),
    STRATEGIST("strategist"),
    VISIONARY("visionary"),
    LEADER("leader"),
    BANKER("banker"),
    SCIENTIST("scientist"),
    TRADESMAN("tradesman");

    private final String answer;


    CadtArchetypes(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public static CadtArchetypes fromAnswer(String answer) {
        for (CadtArchetypes archetype : CadtArchetypes.values()) {
            if (archetype.getAnswer().equalsIgnoreCase(answer)) {
                return archetype;
            }
        }
        return null;
    }
}
