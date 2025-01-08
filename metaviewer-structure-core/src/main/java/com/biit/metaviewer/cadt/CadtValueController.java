package com.biit.metaviewer.cadt;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.provider.CadtProvider;
import com.biit.metaviewer.types.BooleanType;
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
public class CadtValueController extends CadtController {

    private static final String PIVOTVIEWER_LINK = "/cadt";
    private static final String PIVOTVIEWER_FILE = "cadt.cxml";
    private static final String METAVIEWER_FILE = "cadt.json";


    @Value("${metaviewer.samples}")
    private String outputFolder;

    public CadtValueController(CadtProvider cadtProvider) {
        super(cadtProvider);
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
        facets.addAll(createCadtValueFacets(droolsSubmittedForm.getChildrenRecursive(DroolsSubmittedQuestion.class), droolsSubmittedForm.getSubmittedAt()));
    }


    private List<Facet<?>> createCadtValueFacets(List<DroolsSubmittedQuestion> questions, LocalDateTime submittedTime) {
        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();


        for (DroolsSubmittedQuestion question : questions) {
            final List<CadtArchetypes> selectedArchetypes = new ArrayList<>();
            final List<CadtCompetences> selectedCompetences = new ArrayList<>();
            //Adding archetypes.
            if (Objects.equals(question.getName(), CadtQuestion.QUESTION1.getTag())
                    || Objects.equals(question.getName(), CadtQuestion.QUESTION3.getTag())
                    || Objects.equals(question.getName(), CadtQuestion.QUESTION4.getTag())
                    || Objects.equals(question.getName(), CadtQuestion.QUESTION6.getTag())) {
                facets.add(new Facet<>(question.getAnswers().iterator().next(), new BooleanType(true)));
                selectedArchetypes.add(CadtArchetypes.fromAnswer(question.getAnswers().iterator().next()));
            }

            //Adding competences
            if (Objects.equals(question.getName(), CadtQuestion.COMPETENCES.getTag())) {
                for (String answer : question.getAnswers()) {
                    facets.add(new Facet<>(answer, new BooleanType(true)));
                    selectedCompetences.add(CadtCompetences.fromAnswer(answer));
                }
            }
        }
        return facets;
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
