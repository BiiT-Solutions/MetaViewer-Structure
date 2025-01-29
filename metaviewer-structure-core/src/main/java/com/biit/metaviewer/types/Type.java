package com.biit.metaviewer.types;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface Type {

    String getValue();

    @JsonIgnore
    String getMetaViewerDefinition();
}
