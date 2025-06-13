package com.biit.metaviewer.controllers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.Item;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.exceptions.FormFactsNotFoundException;
import com.biit.metaviewer.exceptions.InvalidFormException;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.providers.FormProvider;
import com.biit.metaviewer.types.BooleanType;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import com.biit.metaviewer.types.StringType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Controller
public class FormController {
    private static final String PIVOTVIEWER_IMAGE_FILE = "./five_colors/five_colors.dzc";
    private static final String RED_COLOR_TAG = "#1";
    private static final String ORANGE_COLOR_TAG = "#2";
    private static final String YELLOW_COLOR_TAG = "#3";
    private static final String LIGHT_GREEN_COLOR_TAG = "#4";
    private static final String DARK_GREEN_COLOR_TAG = "#5";

    private static final String FORM_COLORS_FILE = "forms.properties";
    private static final int DEFAULT_RED_LIMIT = 20;
    private static final int DEFAULT_ORANGE_LIMIT = 40;
    private static final int DEFAULT_YELLOW_LIMIT = 60;
    private static final int DEFAULT_LIGHT_GREEN_LIMIT = 80;

    protected static final String CREATED_AT_FACET = "submittedAt";

    @Value("${metaviewer.samples}")
    private String outputFolder;

    private final ObjectMapper objectMapper;

    private final FormProvider formProvider;

    private Properties formsProperties;

    @Value("${forms.enabled}")
    private List<String> formsEnabled;

    public FormController(ObjectMapper objectMapper, FormProvider formProvider) {
        this.objectMapper = objectMapper;
        this.formProvider = formProvider;
    }

    public FormProvider getFormProvider() {
        return formProvider;
    }

    public String getMetaviewerFileName(String formName) {
        return normalizeFormName(formName) + ".json";
    }

    public String getPivotviewerFileName(String formName) {
        return normalizeFormName(formName) + ".cxml";
    }

    protected String getPivotViewerLink(String formName) {
        return "/" + normalizeFormName(formName);
    }

    protected List<FacetCategory> createFacetsCategories(DroolsSubmittedForm droolsSubmittedForm) {
        final List<FacetCategory> facetCategories = new ArrayList<>();
        facetCategories.add(new FacetCategory(CREATED_AT_FACET, DateTimeType.PIVOT_VIEWER_DEFINITION));
        final Map<String, Object> formVariables = droolsSubmittedForm.getFormVariables().get("/DroolsSubmittedForm[@label='"
                + droolsSubmittedForm.getName() + "']");
        for (String variable : formVariables.keySet()) {
            facetCategories.add(new FacetCategory(variable, NumberType.PIVOT_VIEWER_DEFINITION));
        }
        return facetCategories;
    }

    private List<Facet<?>> createScoreFacets(Map<String, Object> formVariables) {
        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();

        for (Map.Entry<String, Object> variable : formVariables.entrySet()) {
            final String value = variable.getValue().toString();
            try {
                facets.add(new Facet<>(variable.getKey(), new DateTimeType(LocalDateTime.parse(value))));
            } catch (DateTimeException e) {
                try {
                    facets.add(new Facet<>(variable.getKey(), new NumberType(Double.parseDouble(value))));
                } catch (NumberFormatException e1) {
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        facets.add(new Facet<>(variable.getKey(), new BooleanType(Boolean.parseBoolean(value))));
                    } else {
                        facets.add(new Facet<>(variable.getKey(), new StringType(value)));
                    }
                }
            }
        }
        return facets;
    }

    public Collection readSamplesFolder(String formName) {
        try {
            return objectMapper.readValue(new File(outputFolder + File.separator + getMetaviewerFileName(formName)), Collection.class);
        } catch (IOException e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
            throw new FormFactsNotFoundException(this.getClass(), "No facts can be found for '" + formName + "' form.");
        }
    }

    public void populateSamplesFolder(String formName) {
        populateSamplesFolder(createCollection(formName), formName);
    }

    protected void populateSamplesFolder(Collection collection, String formName) {
        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFolder
                    + File.separator + getPivotviewerFileName(formName), false), StandardCharsets.UTF_8)))) {
                out.println(ObjectMapperFactory.generateXml(collection));
            }
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFolder
                    + File.separator + getMetaviewerFileName(formName), false), StandardCharsets.UTF_8)))) {
                out.println(ObjectMapperFactory.generateJson(collection));
            }
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }

    public synchronized void newFormReceived(DroolsSubmittedForm droolsSubmittedForm) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Collection storedCollection;
        try {
            storedCollection = readSamplesFolder(droolsSubmittedForm.getName());
            if (storedCollection != null) {
                MetaViewerLogger.debug(this.getClass(), "Updating existing collection.");
                updateCollection(storedCollection, droolsSubmittedForm);
            } else {
                MetaViewerLogger.debug(this.getClass(), "Creating a new collection.");
                storedCollection = createCollection(getFormProvider().getAll(null, droolsSubmittedForm.getName()));
            }
        } catch (Exception e) {
            MetaViewerLogger.debug(this.getClass(), "Creating a new collection.");
            storedCollection = createCollection(getFormProvider().getAll(null, droolsSubmittedForm.getName()));
        } finally {
            stopWatch.stop();
            MetaViewerLogger.info(this.getClass(), "Collection updated in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }

        populateSamplesFolder(storedCollection, droolsSubmittedForm.getName());
    }

    public Collection createCollection(String formName) {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return createCollection(getFormProvider().getAll(null, formName));
        } finally {
            stopWatch.stop();
            MetaViewerLogger.info(this.getClass(), "Collection for '" + formName + "' created in '" + stopWatch.getTotalTimeMillis() + "' ms");
        }
    }

    public Collection createCollection(List<DroolsSubmittedForm> droolsSubmittedForms) {
        if (droolsSubmittedForms.isEmpty()) {
            throw new FormFactsNotFoundException(this.getClass(), "No facts found");
        }
        final Collection collection = new Collection(droolsSubmittedForms.get(0).getName(), PIVOTVIEWER_IMAGE_FILE);
        MetaViewerLogger.debug(this.getClass(), "Creating a new collection with '{}' elements.", droolsSubmittedForms.size());
        collection.getFacetCategories().addAll(createFacetsCategories(droolsSubmittedForms.get(0)));
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
        //If it has data, include it. All has submittedAt facet, so size > 1.
        if (item.getFacets().size() > 1) {
            //Remove old item for the same user. Avoid duplicates.
            collection.getItems().getItems().removeIf(i -> Objects.equals(i.getName(), item.getName()));
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

    protected void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables) {
        facets.addAll(createScoreFacets(formVariables));
    }

    protected String getColor(String formName, Map<String, Object> formVariables) {
        double totalScore = 0;
        for (Map.Entry<String, Object> entry : formVariables.entrySet()) {
            totalScore += Double.parseDouble(entry.getValue().toString());
        }
        MetaViewerLogger.info(this.getClass(), "Total color for '" + formName + "' is '" + totalScore + "'.");
        return getScoreColor(formName, totalScore);
    }

    protected Item generateItem(DroolsSubmittedForm droolsSubmittedForm) {
        if (droolsSubmittedForm == null) {
            throw new InvalidFormException("DroolsSubmittedForm is null.");
        }
        final Map<String, Object> formVariables = droolsSubmittedForm.getFormVariables().get("/DroolsSubmittedForm[@label='"
                + droolsSubmittedForm.getName() + "']");
        if (formVariables == null) {
            throw new InvalidFormException("Form has not variables.");
        }

        final List<Facet<?>> facets = new ArrayList<>(basicData(droolsSubmittedForm.getSubmittedAt()));
        populateFacets(facets, droolsSubmittedForm, formVariables);
        final Item item = new Item(getColor(droolsSubmittedForm.getName(), formVariables),
                getPivotViewerLink(droolsSubmittedForm.getName()), droolsSubmittedForm.getSubmittedBy());
        item.getFacets().addAll(facets);
        return item;
    }

    private Properties getFormsProperties() {
        if (formsProperties == null) {
            final Resource resource = new ClassPathResource("/" + FORM_COLORS_FILE);
            try {
                formsProperties = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
                MetaViewerLogger.severe(this.getClass(), "Cannot load form colors file.");
            }
        }
        return formsProperties;
    }

    private String normalizeFormName(String formName) {
        if (formName == null) {
            return null;
        }
        return "form." + formName.toLowerCase().replace(" ", "-");
    }

    protected int getRedColorLimit(String formName) {
        final Properties formsProperties = getFormsProperties();
        if (formsProperties != null) {
            try {
                return Integer.parseInt(formsProperties.getProperty(normalizeFormName(formName) + ".red.limit"));
            } catch (NumberFormatException e) {
                MetaViewerLogger.errorMessage(this.getClass(), e);
            }
        }
        return DEFAULT_RED_LIMIT;
    }

    protected int getOrangeColorLimit(String formName) {
        final Properties formsProperties = getFormsProperties();
        if (formsProperties != null) {
            try {
                return Integer.parseInt(formsProperties.getProperty(normalizeFormName(formName) + ".orange.limit"));
            } catch (NumberFormatException e) {
                MetaViewerLogger.errorMessage(this.getClass(), e);
            }
        }
        return DEFAULT_ORANGE_LIMIT;
    }

    protected int getYellowColorLimit(String formName) {
        final Properties formsProperties = getFormsProperties();
        if (formsProperties != null) {
            try {
                return Integer.parseInt(formsProperties.getProperty(normalizeFormName(formName) + ".yellow.limit"));
            } catch (NumberFormatException e) {
                MetaViewerLogger.errorMessage(this.getClass(), e);
            }
        }
        return DEFAULT_YELLOW_LIMIT;
    }

    protected int getLightGreenColorLimit(String formName) {
        final Properties formsProperties = getFormsProperties();
        if (formsProperties != null) {
            try {
                return Integer.parseInt(formsProperties.getProperty(normalizeFormName(formName) + ".light-green.limit"));
            } catch (NumberFormatException e) {
                MetaViewerLogger.errorMessage(this.getClass(), e);
            }
        }
        return DEFAULT_LIGHT_GREEN_LIMIT;
    }

    protected String getScoreColor(String formName, double score) {
        if (score < getRedColorLimit(formName)) {
            MetaViewerLogger.debug(this.getClass(), "Score is '" + score + "' that is below that '" + getRedColorLimit(formName)
                    + "'. So color is Red.");
            return RED_COLOR_TAG;
        }
        if (score < getOrangeColorLimit(formName)) {
            MetaViewerLogger.debug(this.getClass(), "Score is '" + score + "' that is below that '" + getOrangeColorLimit(formName)
                    + "'. So color is Orange.");
            return ORANGE_COLOR_TAG;
        }
        if (score < getYellowColorLimit(formName)) {
            MetaViewerLogger.debug(this.getClass(), "Score is '" + score + "' that is below that '" + getYellowColorLimit(formName)
                    + "'. So color is Yellow.");
            return YELLOW_COLOR_TAG;
        }
        if (score < getLightGreenColorLimit(formName)) {
            MetaViewerLogger.debug(this.getClass(), "Score is '" + score + "' that is below that '" + getLightGreenColorLimit(formName)
                    + "'. So color is Light Green.");
            return LIGHT_GREEN_COLOR_TAG;
        }
        MetaViewerLogger.debug(this.getClass(), "Score is '" + score + "' that is above '" + getLightGreenColorLimit(formName)
                + "'. So color is Dark Green.");
        return DARK_GREEN_COLOR_TAG;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void populateSamplesFolder() {
        try {
            for (String formName : formsEnabled) {
                MetaViewerLogger.info(this.getClass(), "Populating form '" + formName + "'");
                populateSamplesFolder(formName);
            }
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }
}
