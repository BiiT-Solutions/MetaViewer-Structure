package com.biit.metaviewer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JacksonXmlRootElement(localName = "Items")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
    private static final int HASH_KEY = 31;

    @JacksonXmlProperty(isAttribute = true, localName = "ImgBase")
    private String imageBase;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Item")
    private List<Item> items;

    public Items() {
    }

    public Items(String imageBase) {
        this.imageBase = imageBase;
        this.items = new ArrayList<>();
    }

    public void setImageBase(String imageBase) {
        this.imageBase = imageBase;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getImageBase() {
        return imageBase;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Items items1)) {
            return false;
        }

        return Objects.equals(imageBase, items1.imageBase) && Objects.equals(items, items1.items);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(imageBase);
        result = HASH_KEY * result + Objects.hashCode(items);
        return result;
    }
}
