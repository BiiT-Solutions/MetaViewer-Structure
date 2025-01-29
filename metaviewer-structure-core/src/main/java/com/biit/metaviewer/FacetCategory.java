package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "FacetCategory")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacetCategory {
    private static final int HASH_KEY = 37;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "Type")
    private String type;

    public FacetCategory() {
    }

    public FacetCategory(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof FacetCategory that)) {
            return false;
        }

        return name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = HASH_KEY * result + type.hashCode();
        return result;
    }
}
