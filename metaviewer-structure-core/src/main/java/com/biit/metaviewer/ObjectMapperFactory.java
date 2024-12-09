package com.biit.metaviewer;

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
