package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.providers.FormProvider;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CadtScoreController extends CadtController {

    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "cadt-score.cxml";
    private static final String METAVIEWER_FILE = "cadt-score.json";

    public CadtScoreController(ObjectMapper objectMapper, FormProvider formProvider) {
        super(objectMapper, formProvider);
    }


    @Override
    protected void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables) {
        facets.addAll(createCadtScoreFacets(formVariables));
    }

    @Override
    protected String getPivotViewerLink(String formName) {
        return PIVOTVIEWER_LINK;
    }

    @Override
    public String getMetaviewerFileName(String formName) {
        return METAVIEWER_FILE;
    }

    @Override
    public String getPivotviewerFileName(String formName) {
        return PIVOTVIEWER_FILE;
    }


    @Override
    protected List<FacetCategory> createFacetsCategories(DroolsSubmittedForm droolsSubmittedForm) {
        final List<FacetCategory> facetCategories = new ArrayList<>();
        facetCategories.add(new FacetCategory(CREATED_AT_FACET, DateTimeType.PIVOT_VIEWER_DEFINITION));
        for (CadtVariables variable : CadtVariables.values()) {
            facetCategories.add(new FacetCategory(variable.getVariable(), NumberType.PIVOT_VIEWER_DEFINITION));
        }
        return facetCategories;
    }


    private List<Facet<?>> createCadtScoreFacets(Map<String, Object> formVariables) {
        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();

        for (CadtVariables variable : CadtVariables.values()) {
            final Double value = (Double) formVariables.get(variable.getVariable());
            if (value != null) {
                facets.add(new Facet<>(variable.getVariable(), new NumberType(value)));
            }
        }
        return facets;
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void populateSamplesFolder() {
        try {
            populateSamplesFolder(CadtController.FORM_NAME);
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }

}
