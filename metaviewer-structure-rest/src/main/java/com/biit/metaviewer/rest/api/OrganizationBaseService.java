package com.biit.metaviewer.rest.api;

import com.biit.metaviewer.Collection;
import com.biit.metaviewer.controllers.FormController;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.security.core.Authentication;

public class OrganizationBaseService {

    private final MetaViewerSecurityService securityService;

    public OrganizationBaseService(MetaViewerSecurityService securityService) {
        this.securityService = securityService;
    }

    protected Collection getCollection(Authentication authentication, FormController controller, String formName) {
        final Collection collection = controller.readSamplesFolder(formName);
        final IUserOrganization organization = securityService.getRequiredUserOrganization(authentication,
                securityService.getAdminPrivilege(), securityService.getEditorPrivilege());
        if (organization != null) {
            return collection.filterByOrganization(organization.getName());
        }
        return collection;
    }
}
