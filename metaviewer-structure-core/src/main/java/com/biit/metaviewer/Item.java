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

    @JacksonXmlProperty(isAttribute = true, localName = "Organization")
    private String organization;

    @JacksonXmlElementWrapper(localName = "Facets")
    @JacksonXmlProperty(localName = "Facet")
    private List<Facet<?>> facets;

    public Item() {
    }

    public Item(String img, String href, String name, String organization) {
        this.id = UUID.randomUUID();
        this.img = img;
        this.href = href;
        this.name = name;
        this.organization = organization;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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
