package com.biit.metaviewer.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberType implements Type {
    public static final String PIVOT_VIEWER_DEFINITION = "Number";

    @JacksonXmlProperty(isAttribute = true, localName = "Value")
    private final Double value;

    public NumberType(Double value) {
        this.value = value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    @JsonIgnore
    @Override
    public String getMetaViewerDefinition() {
        return PIVOT_VIEWER_DEFINITION;
    }

}
