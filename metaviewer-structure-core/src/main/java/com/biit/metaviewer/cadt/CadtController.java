package com.biit.metaviewer.cadt;

import com.biit.metaviewer.controllers.FormController;
import com.biit.metaviewer.providers.CadtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public abstract class CadtController extends FormController {

    public static final String FORM_NAME = "CADT_Score";

    private static final String FORM_SCORE_VARIABLE = "Score";

    private static final int RED_COLOR_LIMIT = 100;
    private static final int ORANGE_COLOR_LIMIT = 250;
    private static final int YELLOW_COLOR_LIMIT = 350;
    private static final int LIGHT_GREEN_COLOR_LIMIT = 450;

    protected CadtController(ObjectMapper objectMapper, CadtProvider cadtProvider) {
        super(objectMapper, cadtProvider);
    }


    @Override
    public String getFormName() {
        return FORM_NAME;
    }

    protected String getColor(Map<String, Object> formVariables) {
        return getScoreColor((double) formVariables.get(FORM_SCORE_VARIABLE));
    }

    @Override
    protected int getRedColorLimit() {
        return RED_COLOR_LIMIT;
    }

    @Override
    protected int getOrangeColorLimit() {
        return ORANGE_COLOR_LIMIT;
    }

    @Override
    protected int getYellowColorLimit() {
        return YELLOW_COLOR_LIMIT;
    }

    @Override
    protected int getLightGreenColorLimit() {
        return LIGHT_GREEN_COLOR_LIMIT;
    }
}
