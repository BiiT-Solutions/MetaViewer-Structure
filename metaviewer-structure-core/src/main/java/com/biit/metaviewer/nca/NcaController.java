package com.biit.metaviewer.nca;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.controllers.FormController;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.providers.NcaProvider;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class NcaController extends FormController {
    public static final String FORM_NAME = "NCA";

    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "nca.cxml";
    private static final String METAVIEWER_FILE = "nca.json";


    private static final int RED_COLOR_LIMIT = -10;
    private static final int ORANGE_COLOR_LIMIT = -3;
    private static final int YELLOW_COLOR_LIMIT = 4;
    private static final int LIGHT_GREEN_COLOR_LIMIT = 11;

    public NcaController(ObjectMapper objectMapper, NcaProvider ncaProvider) {
        super(objectMapper, ncaProvider);
    }

    @Override
    public String getFormName() {
        return FORM_NAME;
    }

    @Override
    protected String getPivotViewerLink() {
        return PIVOTVIEWER_LINK;
    }

    @Override
    public String getMetaviewerFileName() {
        return METAVIEWER_FILE;
    }

    @Override
    public String getPivotviewerFileName() {
        return PIVOTVIEWER_FILE;
    }

    @Override
    protected List<FacetCategory> createFacetsCategories() {
        final List<FacetCategory> facetCategories = new ArrayList<>();
        facetCategories.add(new FacetCategory(CREATED_AT_FACET, DateTimeType.PIVOT_VIEWER_DEFINITION));
        for (NcaVariables variable : NcaVariables.values()) {
            facetCategories.add(new FacetCategory(variable.getVariable(), NumberType.PIVOT_VIEWER_DEFINITION));
        }
        return facetCategories;
    }


    private List<Facet<?>> createNcaScoreFacets(Map<String, Object> formVariables) {
        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();

        for (NcaVariables variable : NcaVariables.values()) {
            final Double value = (Double) formVariables.get(variable.getVariable());
            if (value != null) {
                facets.add(new Facet<>(variable.getVariable(), new NumberType(value)));
            }
        }
        return facets;
    }


    @Override
    protected void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables) {
        facets.addAll(createNcaScoreFacets(formVariables));
    }

    @Override
    protected String getColor(Map<String, Object> formVariables) {
        double totalScore = 0;
        for (Map.Entry<String, Object> entry : formVariables.entrySet()) {
            totalScore += Double.parseDouble(entry.getValue().toString());
        }
        return getScoreColor(totalScore);
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

    @Scheduled(cron = "0 0 2 * * *")
    public void populateSamplesFolder() {
        try {
            populateSamplesFolder(createCollection());
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }

}
