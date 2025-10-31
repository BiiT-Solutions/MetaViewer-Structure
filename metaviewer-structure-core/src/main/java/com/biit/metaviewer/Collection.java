package com.biit.metaviewer;

/*-
 * #%L
 * MetaViewer Structure (Core)
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
import java.util.Objects;

@JacksonXmlRootElement(localName = "Collection")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Collection {
    private static final int HASH_KEY = 31;

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private static final String XMLNS = "http://schemas.microsoft.com/collection/metadata/2009";

    @JacksonXmlProperty(isAttribute = true, localName = "SchemaVersion")
    private static final String SCHEMA_VERSION = "1.0";

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
        return XMLNS;
    }

    public String getSchemaVersion() {
        return SCHEMA_VERSION;
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

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Collection that)) {
            return false;
        }
        return name.equals(that.name) && facetCategories.equals(that.facetCategories) && items.equals(that.items);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = HASH_KEY * result + facetCategories.hashCode();
        result = HASH_KEY * result + items.hashCode();
        return result;
    }

    public Collection filterByOrganization(String organization) {
        final Collection collection = new Collection();
        collection.setName(this.getName());
        collection.setFacetCategories(this.getFacetCategories());
        collection.setCreatedAt(this.getCreatedAt());
        final Items filteredItems = new Items();
        collection.setItems(filteredItems);
        filteredItems.setItems(this.getItems().getItems().stream().filter(item -> Objects.equals(item.getOrganization(), organization)).toList());
        return collection;
    }
}
