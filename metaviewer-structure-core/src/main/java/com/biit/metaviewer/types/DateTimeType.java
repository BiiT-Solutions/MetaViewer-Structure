package com.biit.metaviewer.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateTimeType implements Type {
    public static final String PIVOT_VIEWER_DEFINITION = "DateTime";
    public static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);

    @JacksonXmlProperty(isAttribute = true, localName = "Value")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_TIME_PATTERN)
    private LocalDateTime value;

    public DateTimeType() {

    }

    public DateTimeType(LocalDateTime value) {
        this.value = value;
    }

    public void setValue(LocalDateTime value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        if (value == null) {
            return null;
        }
        return value.format(LOCAL_DATE_TIME_FORMATTER);
    }

    @Override
    public String getMetaViewerDefinition() {
        return PIVOT_VIEWER_DEFINITION;
    }
}
