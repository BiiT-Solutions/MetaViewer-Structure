package com.biit.metaviewer.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BooleanType implements Type {
    public static final String PIVOT_VIEWER_DEFINITION = "Boolean";

    @JacksonXmlProperty(isAttribute = true, localName = "Value")
    private Boolean value;

    public BooleanType() {

    }

    public BooleanType(Boolean value) {
        this.value = value;
    }

    public void setValue(Boolean value) {
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
        if (!(o instanceof BooleanType that)) {
            return false;
        }

        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
