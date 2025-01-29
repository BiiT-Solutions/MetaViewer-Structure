package com.biit.metaviewer.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberType implements Type {
    public static final String PIVOT_VIEWER_DEFINITION = "Number";

    @JacksonXmlProperty(isAttribute = true, localName = "Value")
    private Double value;

    public NumberType() {

    }

    public NumberType(Double value) {
        this.value = value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    @JsonIgnore
    @Override
    public String getMetaViewerDefinition() {
        return PIVOT_VIEWER_DEFINITION;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof NumberType that)) {
            return false;
        }

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
