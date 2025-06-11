package com.biit.metaviewer.rest.api;


import com.biit.metaviewer.Collection;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.nca.NcaController;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nca")
public class NcaServices {

    private final NcaController ncaController;

    public NcaServices(NcaController ncaController) {
        this.ncaController = ncaController;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets NCA result as json.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection createScoreJson(Authentication authentication, HttpServletResponse response) {
        return ncaController.readSamplesFolder();
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Gets NCA score result as xml.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public String createScoreXml(Authentication authentication, HttpServletResponse response) throws JsonProcessingException {
        return ObjectMapperFactory.generateXml(ncaController.readSamplesFolder());
    }


    @PreAuthorize("hasAnyAuthority(@securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Regenerates NCA result as xml and json and stores it to a file.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(value = "/refresh", produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void refreshScore(Authentication authentication, HttpServletResponse response) {
        new Thread(ncaController::populateSamplesFolder).start();
    }
}
