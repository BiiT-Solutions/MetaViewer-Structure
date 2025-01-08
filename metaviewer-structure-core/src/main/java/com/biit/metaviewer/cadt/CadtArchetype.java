package com.biit.metaviewer.cadt;

public enum CadtArchetype {
    RECEPTIVE("receptive"),
    INNOVATOR("innovator"),
    STRATEGIST("strategist"),
    VISIONARY("visionary"),
    LEADER("leader"),
    BANKER("banker"),
    SCIENTIST("scientist"),
    TRADESMAN("tradesman");

    private final String tag;


    CadtArchetype(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static CadtArchetype fromTag(String tag) {
        for (CadtArchetype archetype : CadtArchetype.values()) {
            if (archetype.getTag().equalsIgnoreCase(tag)) {
                return archetype;
            }
        }
        return null;
    }
}
