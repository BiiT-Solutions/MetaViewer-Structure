package com.biit.metaviewer.rest.api;

/*-
 * #%L
 * MetaViewer Structure (Rest)
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
