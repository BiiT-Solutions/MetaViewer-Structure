package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "Collection")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Collection {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private final String xmlns = "http://schemas.microsoft.com/collection/metadata/2009";

    @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
    private final String schemaVersion = "1.0";

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private final String name;

    @JacksonXmlElementWrapper(localName = "FacetCategories")
    @JacksonXmlProperty(localName = "FacetCategory")
    private final List<FacetCategory> facetCategories;

    @JacksonXmlProperty(localName = "Items")
    private final Items items;

    public Collection(String name, String imageBase) {
        this.name = name;
        this.items = new Items(imageBase);
        this.facetCategories = new ArrayList<>();
    }

    public String getXmlns() {
        return xmlns;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public String getName() {
        return name;
    }

    public List<FacetCategory> getFacetCategories() {
        return facetCategories;
    }

    public Items getItems() {
        return items;
    }
}
