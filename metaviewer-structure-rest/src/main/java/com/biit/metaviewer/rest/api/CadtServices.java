package com.biit.metaviewer.rest.api;


import com.biit.metaviewer.Collection;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.cadt.CadtScoreController;
import com.biit.metaviewer.cadt.CadtValueController;
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
    private final CadtValueController cadtValueController;

    public CadtServices(CadtScoreController cadtScoreController, CadtValueController cadtValueController) {
        this.cadtScoreController = cadtScoreController;
        this.cadtValueController = cadtValueController;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT score result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/scores/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createScoreJson(Authentication authentication, HttpServletResponse response) {
        return cadtScoreController.readSamplesFolder();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT score result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/scores/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createScoreXml(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(cadtScoreController.readSamplesFolder());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Regenerates CADT score result as xml and json and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/scores/refresh", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void refreshScore(Authentication authentication, HttpServletResponse response) {
        cadtScoreController.populateSamplesFolder();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createJson(Authentication authentication, HttpServletResponse response) {
        return cadtValueController.readSamplesFolder();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets CADT result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createXml(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(cadtValueController.readSamplesFolder());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Regenerates CADT result as and json and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/refresh", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public void refresh(Authentication authentication, HttpServletResponse response) {
        cadtValueController.populateSamplesFolder();
    }
}
