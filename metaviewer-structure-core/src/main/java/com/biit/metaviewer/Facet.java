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

import com.biit.metaviewer.types.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Facet")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = FacetSerializer.class)
@JsonDeserialize(using = FacetDeserializer.class)
public class Facet<E extends Type> {
    private static final int HASH_KEY = 31;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    private E type;

    public Facet() {
    }

    public Facet(String name, E type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(E type) {
        this.type = type;
    }

    @JacksonXmlProperty(isAttribute = true)
    public String getName() {
        return name;
    }

    public E getType() {
        return type;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Facet<?> facet)) {
            return false;
        }
        return name.equals(facet.name) && type.equals(facet.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = HASH_KEY * result + type.hashCode();
        return result;
    }
}
