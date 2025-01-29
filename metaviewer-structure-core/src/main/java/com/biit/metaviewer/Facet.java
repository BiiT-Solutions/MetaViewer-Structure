package com.biit.metaviewer;

import com.biit.metaviewer.types.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Facet")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FacetSerializer.class)
@JsonDeserialize(using = FacetDeserializer.class)
public class Facet<E extends Type> {
    private static final int HASH_KEY = 31;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    private E type;

    public Facet() {
    }

    public Facet(String name, E type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(E type) {
        this.type = type;
    }

    @JacksonXmlProperty(isAttribute = true)
    public String getName() {
        return name;
    }

    public E getType() {
        return type;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Facet<?> facet)) {
            return false;
        }
        return name.equals(facet.name) && type.equals(facet.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = HASH_KEY * result + type.hashCode();
        return result;
    }
}
