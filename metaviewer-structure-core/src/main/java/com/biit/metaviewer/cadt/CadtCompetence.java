package com.biit.metaviewer.cadt;

public enum CadtCompetence {
    DISCIPLINE("discipline"),
    CLIENT_ORIENTED("client-oriented"),
    ENGAGEMENT("engagement"),
    COOPERATION("cooperation"),
    LEADERSHIP("leadership"),
    RELATIONSHIPS("building-and-maintaining"),
    DIRECTION("direction"),
    MULTICULTURAL_SENSITIVITY("multicultural-sensitivity"),
    JUDGEMENT("judgement"),
    INDEPENDENCE("independence"),
    INITIATIVE("initiative"),
    GOAL_SETTING("goal-setting"),
    DECISIVENESS("decisiveness"),
    FUTURE("future"),
    COMMUNICATION_SKILLS("communication-skills"),
    BUSINESS_MINDED("business-minded"),
    TENACITY("tenacity"),
    CONSCIENTIOUSNESS("conscientiousness"),
    INTERPERSONAL_SENSITIVITY("interpersonal-sensitivity"),
    FLEXIBILITY("flexibility"),
    PERSUASIVENESS("persuasiveness"),
    INNOVATION("innovation"),
    PROBLEM_ANALYSIS("problem-analysis"),
    PLANIFICATION("planification");


    private final String tag;

    CadtCompetence(String answer) {
        this.tag = answer;
    }

    public String getTag() {
        return tag;
    }

    public static CadtCompetence fromTag(String tag) {
        for (CadtCompetence archetype : CadtCompetence.values()) {
            if (archetype.getTag().equalsIgnoreCase(tag)) {
                return archetype;
            }
        }
        return null;
    }

}
