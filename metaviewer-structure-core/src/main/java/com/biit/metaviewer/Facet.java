package com.biit.metaviewer;

import com.biit.metaviewer.types.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Facet")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FacetSerializer.class)
public class Facet<E extends Type> {

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private final String name;

    private final E type;


    public Facet(String name, E type) {
        this.name = name;
        this.type = type;
    }

    @JacksonXmlProperty(isAttribute = true)
    public String getName() {
        return name;
    }

    public E getType() {
        return type;
    }
}
