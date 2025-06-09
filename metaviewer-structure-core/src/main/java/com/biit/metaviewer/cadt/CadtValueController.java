package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.providers.CadtProvider;
import com.biit.metaviewer.types.BooleanType;
import com.biit.metaviewer.types.DateTimeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class CadtValueController extends CadtController {

    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "cadt.cxml";
    private static final String METAVIEWER_FILE = "cadt.json";


    public CadtValueController(ObjectMapper objectMapper, CadtProvider cadtProvider) {
        super(objectMapper, cadtProvider);
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
    public String getMetaviewerFileName() {
        return METAVIEWER_FILE;
    }

    @Override
    public String getPivotviewerFileName() {
        return PIVOTVIEWER_FILE;
    }


    @Override
    protected void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables) {
        facets.addAll(createCadtValueFacets(droolsSubmittedForm.getChildrenRecursive(DroolsSubmittedQuestion.class)));
    }


    @Override
    protected List<FacetCategory> createFacetsCategories() {
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


    @Scheduled(cron = "0 0 1 * * *")
    public void populateSamplesFolder() {
        try {
            populateSamplesFolder(createCollection());
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }
}
