package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JacksonXmlRootElement(localName = "Item")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private static final int HASH_KEY = 31;

    @JacksonXmlProperty(isAttribute = true, localName = "Id")
    private UUID id;

    @JacksonXmlProperty(isAttribute = true, localName = "Img")
    private String img;

    @JacksonXmlProperty(isAttribute = true, localName = "Href")
    private String href;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    @JacksonXmlElementWrapper(localName = "Facets")
    @JacksonXmlProperty(localName = "Facet")
    private List<Facet<?>> facets;

    public Item() {
    }

    public Item(String img, String href, String name) {
        this.id = UUID.randomUUID();
        this.img = img;
        this.href = href;
        this.name = name;
        facets = new ArrayList<>();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacets(List<Facet<?>> facets) {
        this.facets = facets;
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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Item item)) {
            return false;
        }
        return id.equals(item.id) && Objects.equals(img, item.img) && Objects.equals(href, item.href)
                && name.equals(item.name) && facets.equals(item.facets);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = HASH_KEY * result + Objects.hashCode(img);
        result = HASH_KEY * result + Objects.hashCode(href);
        result = HASH_KEY * result + name.hashCode();
        result = HASH_KEY * result + facets.hashCode();
        return result;
    }
}
