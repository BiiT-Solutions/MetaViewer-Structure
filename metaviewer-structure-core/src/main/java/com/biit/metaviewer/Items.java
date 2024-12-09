package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "Items")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {

    @JacksonXmlProperty(isAttribute = true, localName = "ImgBase")
    private final String imageBase;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Item")
    private final List<Item> items;

    public Items(String imageBase) {
        this.imageBase = imageBase;
        this.items = new ArrayList<>();
    }

    public String getImageBase() {
        return imageBase;
    }

    public List<Item> getItems() {
        return items;
    }
}
