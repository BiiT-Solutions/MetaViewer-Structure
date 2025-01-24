package com.biit.metaviewer.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "class",
        defaultImpl = Type.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanType.class, name = "Boolean"),
        @JsonSubTypes.Type(value = DateTimeType.class, name = "DateTime"),
        @JsonSubTypes.Type(value = NumberType.class, name = "Number"),
        @JsonSubTypes.Type(value = StringType.class, name = "String")
})
public interface Type {

    String getValue();

    @JsonIgnore
    String getMetaViewerDefinition();
}
