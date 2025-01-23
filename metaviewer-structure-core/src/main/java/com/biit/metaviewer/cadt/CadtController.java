package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.Item;
import com.biit.metaviewer.exceptions.InvalidFormException;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.provider.CadtProvider;
import com.biit.metaviewer.types.DateTimeType;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class CadtController {

    public static final String FORM_NAME = "CADT_Score";
    private static final String PIVOTVIEWER_IMAGE_FILE = "./five_colors/five_colors.dzc";

    protected static final String CREATED_AT_FACET = "submittedAt";

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


    public CadtController(CadtProvider cadtProvider) {
        this.cadtProvider = cadtProvider;
    }


    public abstract Collection readSamplesFolder();


    public synchronized void newFormReceived(DroolsSubmittedForm droolsSubmittedForm) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            final Collection storedCollection = readSamplesFolder();
            if (storedCollection != null) {
                MetaViewerLogger.debug(this.getClass(), "Updating existing collection.");
                updateCollection(storedCollection, droolsSubmittedForm);
            } else {
                MetaViewerLogger.debug(this.getClass(), "Creating a new collection.");
                createCollection(cadtProvider.getAll(null));
            }
        } finally {
            stopWatch.stop();
            MetaViewerLogger.info(this.getClass(), "Collection updated in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }
    }

    public Collection createCollection() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return createCollection(cadtProvider.getAll(null));
        } finally {
            stopWatch.stop();
            MetaViewerLogger.info(this.getClass(), "Collection created in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }
    }


    public Collection createCollection(List<DroolsSubmittedForm> droolsSubmittedForms) {
        final Collection collection = new Collection(FORM_NAME, PIVOTVIEWER_IMAGE_FILE);
        MetaViewerLogger.debug(this.getClass(), "Creating a new collection with '{}' elements.", droolsSubmittedForms.size());
        collection.getFacetCategories().addAll(createCadtFacetsCategories());
        for (DroolsSubmittedForm droolsSubmittedForm : droolsSubmittedForms) {
            final Item item = generateItem(droolsSubmittedForm);
            //If it has data, include it. All has submittedAt facet.
            if (item.getFacets().size() > 1) {
                collection.getItems().getItems().add(item);
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

    protected abstract List<FacetCategory> createCadtFacetsCategories();


    protected Item generateItem(DroolsSubmittedForm droolsSubmittedForm) {
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

        final List<Facet<?>> facets = new ArrayList<>(basicData(droolsSubmittedForm.getSubmittedAt()));
        populateFacets(facets, droolsSubmittedForm, formVariables);
        final Item item = new Item(getColor((double) formVariables.get(FORM_SCORE_VARIABLE)), getPivotViewerLink(), droolsSubmittedForm.getSubmittedBy());
        item.getFacets().addAll(facets);
        return item;
    }

    protected abstract String getPivotViewerLink();

    protected abstract void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables);

    protected List<Facet<?>> basicData(LocalDateTime submittedTime) {
        final List<Facet<?>> facets = new ArrayList<>();
        if (submittedTime != null) {
            facets.add(new Facet<>(CREATED_AT_FACET, new DateTimeType(submittedTime)));
        }
        return facets;
    }

    protected String getColor(double score) {
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
}
