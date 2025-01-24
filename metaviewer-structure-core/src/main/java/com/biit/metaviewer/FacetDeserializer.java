package com.biit.metaviewer;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.metaviewer.logger.MetaViewerLogger;
import com.biit.metaviewer.types.Type;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class FacetDeserializer<E extends Type> extends JsonDeserializer<Facet<E>> {

    @Override
    public Facet<E> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final Facet<E> facet = new Facet<>();
        facet.setName(jsonObject.get("Name").asText());

        // Type deserialization
        final JsonNode childrenJson = jsonObject.get("Type");


        if (childrenJson != null) {
            try {
                final Class<E> classType = (Class<E>) Class.forName(childrenJson.get("class").asText());
                facet.setType(ObjectMapperFactory.getObjectMapper().readValue(childrenJson.toPrettyString(), classType));
            } catch (ClassNotFoundException | NullPointerException e) {
                MetaViewerLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
                MetaViewerLogger.errorMessage(this.getClass().getName(), e);
                throw new RuntimeException(e);
            }
        }

        return facet;
    }
}
