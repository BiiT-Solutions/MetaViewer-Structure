package com.biit.metaviewer.providers;

import com.biit.factmanager.client.FactClient;
import org.springframework.stereotype.Service;

@Service
public class CadtProvider extends FormProvider {

    private static final String ELEMENT_NAME = "CADT_Score";

    public CadtProvider(FactClient factClient) {
        super(factClient);
    }

    @Override
    public String getFactElementName() {
        return ELEMENT_NAME;
    }

}
