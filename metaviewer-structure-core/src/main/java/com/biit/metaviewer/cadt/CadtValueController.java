package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.provider.CadtProvider;
import com.biit.metaviewer.types.BooleanType;
import com.biit.metaviewer.types.DateTimeType;
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
import java.util.Objects;

@Controller
public class CadtValueController extends CadtController {

    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "cadt.cxml";
    private static final String METAVIEWER_FILE = "cadt.json";


    @Value("${metaviewer.samples}")
    private String outputFolder;

    private final ObjectMapper objectMapper;

    public CadtValueController(CadtProvider cadtProvider, ObjectMapper objectMapper) {
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
        facets.addAll(createCadtValueFacets(droolsSubmittedForm.getChildrenRecursive(DroolsSubmittedQuestion.class)));
    }


    @Override
    protected List<FacetCategory> createCadtFacetsCategories() {
        final List<FacetCategory> facetCategories = new ArrayList<>();
        facetCategories.add(new FacetCategory(CREATED_AT_FACET, DateTimeType.PIVOT_VIEWER_DEFINITION));
        for (CadtArchetype archetype : CadtArchetype.values()) {
            facetCategories.add(new FacetCategory(archetype.getTag(), BooleanType.PIVOT_VIEWER_DEFINITION));
        }
        for (CadtCompetence competence : CadtCompetence.values()) {
            facetCategories.add(new FacetCategory(competence.getTag(), BooleanType.PIVOT_VIEWER_DEFINITION));
        }
        return facetCategories;
    }


    private List<Facet<?>> createCadtValueFacets(List<DroolsSubmittedQuestion> questions) {
        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();


        final List<CadtArchetype> selectedArchetypes = new ArrayList<>();
        final List<CadtCompetence> selectedCompetences = new ArrayList<>();
        for (DroolsSubmittedQuestion question : questions) {
            //Adding archetypes.
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION1.getTag())
                    || Objects.equals(question.getName(), CadtQuestion.QUESTION3.getTag())
                    || Objects.equals(question.getName(), CadtQuestion.QUESTION4.getTag())
                    || Objects.equals(question.getName(), CadtQuestion.QUESTION6.getTag())) {
                final CadtArchetype archetype = CadtArchetype.fromTag(question.getAnswers().iterator().next());
                if (archetype != null) {
                    facets.add(new Facet<>(archetype.getTag(), new BooleanType(true)));
                    selectedArchetypes.add(archetype);
                }
            }

            //Adding competences
            if (Objects.equals(question.getName(), CadtQuestion.COMPETENCES.getTag())) {
                for (String answer : question.getAnswers()) {
                    final CadtCompetence competence = CadtCompetence.fromTag(answer);
                    if (competence != null) {
                        facets.add(new Facet<>(competence.getTag(), new BooleanType(true)));
                        selectedCompetences.add(competence);
                    }
                }
            }
        }

        //Adding non-selected archetypes.
        for (CadtArchetype archetype : CadtArchetype.values()) {
            if (!selectedArchetypes.contains(archetype)) {
                facets.add(new Facet<>(archetype.getTag(), new BooleanType(false)));
            }
        }

        //Adding non-selected competences.
        for (CadtCompetence competence : CadtCompetence.values()) {
            if (!selectedCompetences.contains(competence)) {
                facets.add(new Facet<>(competence.getTag(), new BooleanType(false)));
            }
        }

        return facets;
    }

    public Collection readSamplesFolder() {
        try {
            return objectMapper.readValue(new File(outputFolder + File.separator + METAVIEWER_FILE), Collection.class);
        } catch (IOException e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
            return null;
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
