package com.biit.metaviewer.cadt;

public enum CadtQuestion {
    QUESTION1("nhm_card_choice1"),
    QUESTION2("nhm_card_choice2"),
    QUESTION3("nhm_card_choice3"),
    QUESTION4("nhm_card_choice4"),
    QUESTION5("nhm_card_choice5"),
    QUESTION6("nhm_card_choice6"),

    COMPETENCES("competency_choice");

    private final String tag;


    CadtQuestion(String answer) {
        this.tag = answer;
    }

    public String getTag() {
        return tag;
    }
}
