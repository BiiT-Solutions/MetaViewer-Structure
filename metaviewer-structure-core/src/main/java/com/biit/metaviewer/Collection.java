package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
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
    private String name;

    @JacksonXmlElementWrapper(localName = "FacetCategories")
    @JacksonXmlProperty(localName = "FacetCategory")
    private List<FacetCategory> facetCategories;

    @JacksonXmlProperty(localName = "Items")
    private Items items;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    public Collection() {
    }

    public Collection(String name, String imageBase) {
        this.name = name;
        this.items = new Items(imageBase);
        this.facetCategories = new ArrayList<>();
        setCreatedAt(LocalDateTime.now());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacetCategories(List<FacetCategory> facetCategories) {
        this.facetCategories = facetCategories;
    }

    public void setItems(Items items) {
        this.items = items;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
