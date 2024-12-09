package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JacksonXmlRootElement(localName = "Item")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    private final UUID id;

    @JacksonXmlProperty(isAttribute = true, localName = "Img")
    private final String img;

    @JacksonXmlProperty(isAttribute = true, localName = "Href")
    private final String href;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private final String name;

    @JacksonXmlElementWrapper(localName = "Facets")
    @JacksonXmlProperty(localName = "Facet")
    private final List<Facet<?>> facets;

    public Item(String img, String href, String name) {
        this.id = UUID.randomUUID();
        this.img = img;
        this.href = href;
        this.name = name;
        facets = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getHref() {
        return href;
    }

    public String getName() {
        return name;
    }

    public List<Facet<?>> getFacets() {
        return facets;
    }
}
