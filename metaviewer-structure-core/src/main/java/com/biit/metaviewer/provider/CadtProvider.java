package com.biit.metaviewer.provider;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.factmanager.client.FactClient;
import com.biit.factmanager.client.SearchParameters;
import com.biit.factmanager.dto.FactDTO;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CadtProvider {

    private static final String FACT_TYPE = "DroolsResultForm";
    private static final String GROUP = "processedForm";
    private static final String ELEMENT_NAME = "CADT_Score";
    private static final String APPLICATION = "BaseFormDroolsEngine";

    private final FactClient factClient;

    public CadtProvider(FactClient factClient) {
        this.factClient = factClient;
    }

    public List<DroolsSubmittedForm> getAll() {
        final Map<SearchParameters, Object> filter = new HashMap<>();
        filter.put(SearchParameters.FACT_TYPE, FACT_TYPE);
        filter.put(SearchParameters.GROUP, GROUP);
        filter.put(SearchParameters.ELEMENT_NAME, ELEMENT_NAME);
        filter.put(SearchParameters.APPLICATION, APPLICATION);
        return get(filter);
    }

    private List<DroolsSubmittedForm> get(Map<SearchParameters, Object> filter) {
        final List<FactDTO> facts = factClient.get(filter, null);
        return facts.stream().map(fact -> {
            try {
                final DroolsSubmittedForm droolsSubmittedForm = ObjectMapperFactory.getJsonObjectMapper().readValue(fact.getValue(), DroolsSubmittedForm.class);
                if (droolsSubmittedForm.getSubmittedAt() == null) {
                    droolsSubmittedForm.setSubmittedAt(fact.getCreatedAt());
                }
                return droolsSubmittedForm;
            } catch (JsonProcessingException e) {
                MetaViewerLogger.errorMessage(this.getClass(), e);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
