package com.biit.metaviewer.cadt;

/*-
 * #%L
 * MetaViewer Structure (Core)
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.providers.FormProvider;
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


    public CadtValueController(ObjectMapper objectMapper, FormProvider formProvider) {
        super(objectMapper, formProvider);
    }


    @PostConstruct
    public void onStartup() {
        //Update data when started.
        populateSamplesFolder();
    }


    @Override
    protected String getPivotViewerLink(String formName) {
        return PIVOTVIEWER_LINK;
    }

    @Override
    public String getMetaviewerFileName(String formName) {
        return normalizeFormName(METAVIEWER_FILE);
    }

    @Override
    public String getPivotviewerFileName(String formName) {
        return normalizeFormName(PIVOTVIEWER_FILE);
    }


    @Override
    protected void populateFacets(List<Facet<?>> facets, DroolsSubmittedForm droolsSubmittedForm, Map<String, Object> formVariables) {
        facets.addAll(createCadtValueFacets(droolsSubmittedForm.getChildrenRecursive(DroolsSubmittedQuestion.class)));
    }


    @Override
    protected List<FacetCategory> createFacetsCategories(DroolsSubmittedForm droolsSubmittedForm) {
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
            populateSamplesFolder(CadtController.FORM_NAME);
        } catch (Exception e) {
            MetaViewerLogger.errorMessage(this.getClass(), e);
        }
    }
}
