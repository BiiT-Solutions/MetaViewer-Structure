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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperFactory {
    private static XmlMapper xmlObjectMapper;

    private ObjectMapperFactory() {
    }

    public static ObjectMapper getJsonObjectMapper() {
        return com.biit.form.jackson.serialization.ObjectMapperFactory.getObjectMapper();
    }


    public static String generateJson(Object object) throws JsonProcessingException {
        return getJsonObjectMapper().writeValueAsString(object);
    }


    public static ObjectMapper getXmlObjectMapper() {
        if (xmlObjectMapper == null) {
            xmlObjectMapper = XmlMapper.builder()
                    .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).serializationInclusion(JsonInclude.Include.NON_EMPTY)
//                    .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
                    .addModule(new JavaTimeModule())
                    .build();
        }
        return xmlObjectMapper;
    }


    public static String generateXml(Object object) throws JsonProcessingException {
        return getXmlObjectMapper().writeValueAsString(object);
    }

}
