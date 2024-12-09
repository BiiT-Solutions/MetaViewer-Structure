package com.biit.metaviewer;

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
