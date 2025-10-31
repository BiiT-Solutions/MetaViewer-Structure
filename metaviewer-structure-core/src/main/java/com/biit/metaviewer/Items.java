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
