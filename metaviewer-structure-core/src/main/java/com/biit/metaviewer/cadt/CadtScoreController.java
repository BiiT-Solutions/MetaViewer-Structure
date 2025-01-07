package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.Item;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.exceptions.InvalidFormException;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.provider.CadtProvider;
import com.biit.metaviewer.types.BooleanType;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class CadtScoreController {
    private static final String FORM_NAME = "CADT_Score";
    private static final String PIVOTVIEWER_IMAGE_FILE = "./five_colors/five_colors.dzc";
    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "cadt.cxml";
    private static final String CREATED_AT_FACET = "submittedAt";

    private static final String FORM_SCORE_VARIABLE = "Score";

    private static final int RED_COLOR_LIMIT = 100;
    private static final int ORANGE_COLOR_LIMIT = 250;
    private static final int YELLOW_COLOR_LIMIT = 350;
    private static final int LIGHT_GREEN_COLOR_LIMIT = 450;

    private static final String RED_COLOR_TAG = "#1";
    private static final String ORANGE_COLOR_TAG = "#2";
    private static final String YELLOW_COLOR_TAG = "#3";
    private static final String LIGHT_GREEN_COLOR_TAG = "#4";
    private static final String DARK_GREEN_COLOR_TAG = "#5";

    private final CadtProvider cadtProvider;

    @Value("${metaviewer.samples}")
    private String outputFolder;

    public CadtScoreController(CadtProvider cadtProvider) {
        this.cadtProvider = cadtProvider;
    }


    @PostConstruct
    public void onStartup() {
        //Update data when started.
        populateSamplesFolder();
    }

    public Collection createCollection() {
        return createCollection(cadtProvider.getAll());
    }


    public Collection createCollection(List<DroolsSubmittedForm> droolsSubmittedForms) {
        final Collection collection = new Collection(FORM_NAME, PIVOTVIEWER_IMAGE_FILE);
        collection.getFacetCategories().addAll(createCadtFacetsCategories());
        for (DroolsSubmittedForm droolsSubmittedForm : droolsSubmittedForms) {
            collection.getItems().getItems().add(generateItem(droolsSubmittedForm));
        }
        return collection;
    }

    private List<FacetCategory> createCadtFacetsCategories() {
        final List<FacetCategory> facetCategories = new ArrayList<>();
        facetCategories.add(new FacetCategory(CREATED_AT_FACET, DateTimeType.PIVOT_VIEWER_DEFINITION));
        for (CadtVariables variable : CadtVariables.values()) {
            facetCategories.add(new FacetCategory(variable.getVariable(), NumberType.PIVOT_VIEWER_DEFINITION));
        }
        return facetCategories;
    }


    private Item generateItem(DroolsSubmittedForm droolsSubmittedForm) {
        if (droolsSubmittedForm == null) {
            throw new InvalidFormException("DroolsSubmittedForm is null.");
        }
        if (!Objects.equals(droolsSubmittedForm.getName(), FORM_NAME)) {
            throw new InvalidFormException("Form '" + droolsSubmittedForm.getName() + "' is not the correct form.");
        }
        final Map<String, Object> formVariables = droolsSubmittedForm.getFormVariables().get("/DroolsSubmittedForm[@label='" + FORM_NAME + "']");
        if (formVariables == null) {
            throw new InvalidFormException("Form has not variables.");
        }

        final List<Facet<?>> facets = new ArrayList<>();
        facets.addAll(basicData(droolsSubmittedForm.getSubmittedAt()));
        facets.addAll(createCadtScoreFacets(formVariables, droolsSubmittedForm.getSubmittedAt()));
        final Item item = new Item(getColor((double) formVariables.get(FORM_SCORE_VARIABLE)), PIVOTVIEWER_LINK, droolsSubmittedForm.getSubmittedBy());
        item.getFacets().addAll(facets);
        return item;
    }

    private List<Facet<?>> basicData(LocalDateTime submittedTime) {
        final List<Facet<?>> facets = new ArrayList<>();
        if (submittedTime != null) {
            facets.add(new Facet<>(CREATED_AT_FACET, new DateTimeType(submittedTime)));
        }
        return facets;
    }


    private List<Facet<?>> createCadtScoreFacets(Map<String, Object> formVariables, LocalDateTime submittedTime) {
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


    private String getColor(double score) {
        if (score < RED_COLOR_LIMIT) {
            return RED_COLOR_TAG;
        }
        if (score < ORANGE_COLOR_LIMIT) {
            return ORANGE_COLOR_TAG;
        }
        if (score < YELLOW_COLOR_LIMIT) {
            return YELLOW_COLOR_TAG;
        }
        if (score < LIGHT_GREEN_COLOR_LIMIT) {
            return LIGHT_GREEN_COLOR_TAG;
        }
        return DARK_GREEN_COLOR_TAG;
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
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }
}
