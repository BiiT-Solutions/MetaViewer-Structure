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
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.controllers.FormController;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forms")
public class FormServices extends OrganizationBaseService {

    private final FormController formController;

    public FormServices(FormController formController, MetaViewerSecurityService securityService) {
        super(securityService);
        this.formController = formController;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets Form result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{formName}/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createScoreJson(@Parameter(description = "Name of the form", required = true)
                                      @PathVariable("formName") String formName,
                                      Authentication authentication, HttpServletResponse response) {
        return getCollection(authentication, formController, formName);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Gets Form score result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/{formName}/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createScoreXml(@Parameter(description = "Name of the form", required = true)
                                 @PathVariable("formName") String formName,
                                 Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(getCollection(authentication, formController, formName));
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @Operation(summary = "Regenerates Form result as xml and json and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/{formName}/refresh", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void refreshScore(@Parameter(description = "Name of the form", required = true)
                             @PathVariable("formName") String formName,
                             Authentication authentication, HttpServletResponse response) {
        new Thread(() -> formController.populateSamplesFolder(formName)).start();
    }
}
