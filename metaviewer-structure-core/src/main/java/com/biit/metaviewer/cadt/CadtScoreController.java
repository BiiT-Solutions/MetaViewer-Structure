package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.provider.CadtProvider;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CadtScoreController extends CadtController {

    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "cadt-score.cxml";
    private static final String METAVIEWER_FILE = "cadt-score.json";

    @Value("${metaviewer.samples}")
    private String outputFolder;

    private final ObjectMapper objectMapper;

    public CadtScoreController(CadtProvider cadtProvider, ObjectMapper objectMapper) {
        super(cadtProvider);
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void onStartup() {
        //Update data when started.
        populateSamplesFolder();
    }

    @Override
    protected String getPivotViewerLink() {
        return PIVOTVIEWER_LINK;
    }

    @Override
    protected void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables) {
        facets.addAll(createCadtScoreFacets(formVariables));
    }


    @Override
    protected List<FacetCategory> createCadtFacetsCategories() {
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

    public Collection readSamplesFolder() {
        try {
            return objectMapper.readValue(new File(outputFolder + File.separator + METAVIEWER_FILE), Collection.class);
        } catch (IOException e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
            return createCollection();
        }
    }


    @Scheduled(cron = "@midnight")
    public void populateSamplesFolder() {
        try {
            //Add new one
            final Collection collection = createCollection();
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFolder
                    + File.separator + PIVOTVIEWER_FILE, false), StandardCharsets.UTF_8)))) {
                out.println(ObjectMapperFactory.generateXml(collection));
            }
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFolder
                    + File.separator + METAVIEWER_FILE, false), StandardCharsets.UTF_8)))) {
                out.println(ObjectMapperFactory.generateJson(collection));
            }
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }
}
