package com.biit.metaviewer.rest.api;


import com.biit.metaviewer.Collection;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.cadt.CadtController;
import com.biit.metaviewer.cadt.CadtScoreController;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cadt")
public class CadtServices {

    private final CadtScoreController cadtScoreController;
    private final CadtController cadtController;

    public CadtServices(CadtScoreController cadtScoreController, CadtController cadtController) {
        this.cadtScoreController = cadtScoreController;
        this.cadtController = cadtController;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT score result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/score/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createScoreJson(Authentication authentication, HttpServletResponse response) {
        return cadtScoreController.createCollection();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT score result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/score/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createScoreXml(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(cadtScoreController.createCollection());
    }


    @Operation(summary = "Regenerates CADT score result as xml and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/score/public/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void refreshScore(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        cadtScoreController.populateSamplesFolder();
    }

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createJson(Authentication authentication, HttpServletResponse response) {
        return cadtController.createCollection();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createXml(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(cadtController.createCollection());
    }


    @Operation(summary = "Regenerates CADT result as xml and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/public/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void refresh(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        cadtController.populateSamplesFolder();
    }
}
