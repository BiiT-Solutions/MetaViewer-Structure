package com.biit.metaviewer.rest.api;


import com.biit.server.rest.SecurityService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("securityService")
public class MetaViewerSecurityService extends SecurityService {

    private static final String VIEWER = "METAVIEWERSTRUCTURE_VIEWER";
    private static final String ADMIN = "METAVIEWERSTRUCTURE_ADMIN";
    private static final String EDITOR = "METAVIEWERSTRUCTURE_EDITOR";

    private String viewerPrivilege = null;
    private String adminPrivilege = null;
    private String editorPrivilege = null;

    @Override
    public String getViewerPrivilege() {
        if (viewerPrivilege == null) {
            viewerPrivilege = VIEWER.toUpperCase();
        }
        return viewerPrivilege;
    }

    @Override
    public String getAdminPrivilege() {
        if (adminPrivilege == null) {
            adminPrivilege = ADMIN.toUpperCase();
        }
        return adminPrivilege;
    }

    @Override
    public String getEditorPrivilege() {
        if (editorPrivilege == null) {
            editorPrivilege = EDITOR.toUpperCase();
        }
        return editorPrivilege;
    }
}
