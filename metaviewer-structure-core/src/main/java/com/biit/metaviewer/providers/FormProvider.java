package com.biit.metaviewer.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.factmanager.client.FactClient;
import com.biit.factmanager.client.SearchParameters;
import com.biit.factmanager.dto.FactDTO;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FormProvider {
    private static final String FACT_TYPE = "DroolsResultForm";
    private static final String GROUP = "processedForm";
    private static final String APPLICATION = "BaseFormDroolsEngine";
    private static final Boolean LATEST_BY_USER = true;

    private final FactClient factClient;

    protected FormProvider(FactClient factClient) {
        this.factClient = factClient;
    }

    public List<DroolsSubmittedForm> getAll(LocalDateTime from, String formName) {
        final Map<SearchParameters, Object> filter = new EnumMap<>(SearchParameters.class);
        filter.put(SearchParameters.FACT_TYPE, FACT_TYPE);
        filter.put(SearchParameters.GROUP, GROUP);
        filter.put(SearchParameters.ELEMENT_NAME, formName);
        filter.put(SearchParameters.APPLICATION, APPLICATION);
        filter.put(SearchParameters.LATEST_BY_USER, LATEST_BY_USER);
        if (from != null) {
            filter.put(SearchParameters.FROM, from.format(DateTimeFormatter.ISO_DATE_TIME));
        }
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
