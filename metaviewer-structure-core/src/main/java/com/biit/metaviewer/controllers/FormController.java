package com.biit.metaviewer.controllers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.Item;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.exceptions.InvalidFormException;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.providers.FormProvider;
import com.biit.metaviewer.types.DateTimeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class FormController {
    private static final String PIVOTVIEWER_IMAGE_FILE = "./five_colors/five_colors.dzc";
    private static final String RED_COLOR_TAG = "#1";
    private static final String ORANGE_COLOR_TAG = "#2";
    private static final String YELLOW_COLOR_TAG = "#3";
    private static final String LIGHT_GREEN_COLOR_TAG = "#4";
    private static final String DARK_GREEN_COLOR_TAG = "#5";

    protected static final String CREATED_AT_FACET = "submittedAt";

    @Value("${metaviewer.samples}")
    private String outputFolder;

    private final ObjectMapper objectMapper;

    private final FormProvider formProvider;

    public FormController(ObjectMapper objectMapper, FormProvider formProvider) {
        this.objectMapper = objectMapper;
        this.formProvider = formProvider;
    }

    public FormProvider getFormProvider() {
        return formProvider;
    }

    public abstract String getMetaviewerFileName();

    public abstract String getPivotviewerFileName();

    public abstract String getFormName();

    protected abstract String getPivotViewerLink();

    protected abstract List<FacetCategory> createFacetsCategories();

    public Collection readSamplesFolder() {
        try {
            return objectMapper.readValue(new File(outputFolder + File.separator + getMetaviewerFileName()), Collection.class);
        } catch (IOException e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
            return null;
        }
    }

    protected void populateSamplesFolder(Collection collection) {
        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFolder
                    + File.separator + getPivotviewerFileName(), false), StandardCharsets.UTF_8)))) {
                out.println(ObjectMapperFactory.generateXml(collection));
            }
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFolder
                    + File.separator + getMetaviewerFileName(), false), StandardCharsets.UTF_8)))) {
                out.println(ObjectMapperFactory.generateJson(collection));
            }
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }

    public synchronized void newFormReceived(DroolsSubmittedForm droolsSubmittedForm) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            Collection storedCollection = readSamplesFolder();
            if (storedCollection != null) {
                MetaViewerLogger.debug(this.getClass(), "Updating existing collection.");
                updateCollection(storedCollection, droolsSubmittedForm);
            } else {
                MetaViewerLogger.debug(this.getClass(), "Creating a new collection.");
                storedCollection = createCollection(getFormProvider().getAll(null));
            }
            populateSamplesFolder(storedCollection);
        } finally {
            stopWatch.stop();
            MetaViewerLogger.info(this.getClass(), "Collection updated in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }
    }

    public Collection createCollection() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return createCollection(getFormProvider().getAll(null));
        } finally {
            stopWatch.stop();
            MetaViewerLogger.info(this.getClass(), "Collection created in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }
    }

    public Collection createCollection(List<DroolsSubmittedForm> droolsSubmittedForms) {
        final Collection collection = new Collection(getFormName(), PIVOTVIEWER_IMAGE_FILE);
        MetaViewerLogger.debug(this.getClass(), "Creating a new collection with '{}' elements.", droolsSubmittedForms.size());
        collection.getFacetCategories().addAll(createFacetsCategories());
        for (DroolsSubmittedForm droolsSubmittedForm : droolsSubmittedForms) {
            try {
                final Item item = generateItem(droolsSubmittedForm);
                //If it has data, include it. All has submittedAt facet.
                if (item.getFacets().size() > 1) {
                    collection.getItems().getItems().add(item);
                }
            } catch (Exception e) {
                MetaViewerLogger.severe(this.getClass(), "Cannot generate item submitted by '" + droolsSubmittedForm.getSubmittedBy() + "'.");
            }
        }
        collection.setCreatedAt(LocalDateTime.now());
        return collection;
    }

    public void updateCollection(Collection collection, DroolsSubmittedForm droolsSubmittedForm) {
        final Item item = generateItem(droolsSubmittedForm);
        //If it has data, include it. All has submittedAt facet.
        if (item.getFacets().size() > 1) {
            MetaViewerLogger.info(this.getClass(), "Adding one new item to collection.");
            collection.getItems().getItems().add(item);
        } else {
            MetaViewerLogger.debug(this.getClass(), "No new data generated.");
        }
        collection.setCreatedAt(LocalDateTime.now());
    }

    private List<Facet<?>> basicData(LocalDateTime submittedTime) {
        final List<Facet<?>> facets = new ArrayList<>();
        if (submittedTime != null) {
            facets.add(new Facet<>(CREATED_AT_FACET, new DateTimeType(submittedTime)));
        }
        return facets;
    }

    protected abstract void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables);

    protected abstract String getColor(Map<String, Object> formVariables);

    protected Item generateItem(DroolsSubmittedForm droolsSubmittedForm) {
        if (droolsSubmittedForm == null) {
            throw new InvalidFormException("DroolsSubmittedForm is null.");
        }
        if (!Objects.equals(droolsSubmittedForm.getName(), getFormName())) {
            throw new InvalidFormException("Form '" + droolsSubmittedForm.getName() + "' is not the correct form.");
        }
        final Map<String, Object> formVariables = droolsSubmittedForm.getFormVariables().get("/DroolsSubmittedForm[@label='" + getFormName() + "']");
        if (formVariables == null) {
            throw new InvalidFormException("Form has not variables.");
        }

        final List<Facet<?>> facets = new ArrayList<>(basicData(droolsSubmittedForm.getSubmittedAt()));
        populateFacets(facets, droolsSubmittedForm, formVariables);
        final Item item = new Item(getColor(formVariables), getPivotViewerLink(), droolsSubmittedForm.getSubmittedBy());
        item.getFacets().addAll(facets);
        return item;
    }

    protected abstract int getRedColorLimit();

    protected abstract int getOrangeColorLimit();

    protected abstract int getYellowColorLimit();

    protected abstract int getLightGreenColorLimit();

    protected String getScoreColor(double score) {
        if (score < getRedColorLimit()) {
            return RED_COLOR_TAG;
        }
        if (score < getOrangeColorLimit()) {
            return ORANGE_COLOR_TAG;
        }
        if (score < getYellowColorLimit()) {
            return YELLOW_COLOR_TAG;
        }
        if (score < getLightGreenColorLimit()) {
            return LIGHT_GREEN_COLOR_TAG;
        }
        return DARK_GREEN_COLOR_TAG;
    }
}
