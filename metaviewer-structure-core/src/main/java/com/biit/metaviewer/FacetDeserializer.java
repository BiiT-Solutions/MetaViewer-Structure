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

import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.types.BooleanType;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import com.biit.metaviewer.types.StringType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;

public class FacetDeserializer extends JsonDeserializer<Facet<?>> {

    @Override
    public Facet<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        // Type deserialization
        final JsonNode childrenJson = jsonObject.get("Type");

        if (childrenJson != null) {
            try {
                final String value = childrenJson.get("value").asText();
                try {
                    final Facet<DateTimeType> facet = new Facet<>();
                    facet.setType(new DateTimeType(LocalDateTime.parse(value)));
                    facet.setName(jsonObject.get("Name").asText());
                    return facet;
                } catch (DateTimeException e) {
                    try {
                        final Facet<NumberType> facet = new Facet<>();
                        facet.setType(new NumberType(Double.parseDouble(value)));
                        facet.setName(jsonObject.get("Name").asText());
                        return facet;
                    } catch (NumberFormatException e1) {
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            final Facet<BooleanType> facet = new Facet<>();
                            facet.setType(new BooleanType(Boolean.parseBoolean(value)));
                            facet.setName(jsonObject.get("Name").asText());
                            return facet;
                        } else {
                            final Facet<StringType> facet = new Facet<>();
                            facet.setType(new StringType(value));
                            facet.setName(jsonObject.get("Name").asText());
                            return facet;
                        }
                    }
                }
            } catch (Exception e) {
                MetaViewerLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
                MetaViewerLogger.errorMessage(this.getClass().getName(), e);
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
