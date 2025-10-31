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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.io.IOException;

public class FacetSerializer extends JsonSerializer<Facet<?>> {

    @Override
    public void serialize(Facet<?> value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        if (jsonGenerator instanceof ToXmlGenerator) {
            ((ToXmlGenerator) jsonGenerator).setNextIsAttribute(true);
        }
        jsonGenerator.writeFieldName("Name");
        jsonGenerator.writeString(value.getName());
        if (jsonGenerator instanceof ToXmlGenerator) {
            ((ToXmlGenerator) jsonGenerator).setNextIsAttribute(false);
            jsonGenerator.writeObjectField(value.getType().getMetaViewerDefinition(), value.getType());
        } else {
            jsonGenerator.writeObjectField("Type", value.getType());
        }

        jsonGenerator.writeEndObject();
    }
}
