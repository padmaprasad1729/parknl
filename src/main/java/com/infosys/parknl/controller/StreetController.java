package com.infosys.parknl.controller;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.model.StreetDetails;
import com.infosys.parknl.service.StreetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ParkNLConstants.API_VERSION_V1)
public class StreetController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final StreetService streetService;

    public StreetController(StreetService streetService) {
        this.streetService = streetService;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "addStreetDetails",
                            description = "Add Street Details.",
                            value = "{\"streetName\": \"streetName\", \"priceInCentPerMinute\", 10}")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping(value = "/streets")
    public ResponseEntity<StreetDetails> addStreetDetails(@RequestBody StreetDetails streetDetails) {
        StreetDetails response = streetService.addStreetDetails(streetDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "readAllStreetDetails",
                            description = "Get all Street Details.",
                            value = "[{\"streetName\": \"streetName1\", \"priceInCentPerMinute\": 10}, {\"streetName\": \"streetName2\", \"priceInCentPerMinute\": 11}]")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @GetMapping("/streets")
    public ResponseEntity<List<StreetDetails>> readAllStreetDetails() {
        List<StreetDetails> response = streetService.readAllStreetDetails();
        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "readStreetDetails",
                            description = "Get Street Details by streetName.",
                            value = "{\"streetName\": \"streetName\", \"priceInCentPerMinute\": 10}")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @GetMapping("/streets/{streetName}")
    public ResponseEntity<StreetDetails> readStreetDetails(@PathVariable String streetName) {
        StreetDetails response = streetService.findByStreetName(streetName);
        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "updateStreetDetails",
                            description = "Update Street Details by streetName.",
                            value = "[{\"streetName\": \"streetName\", \"priceInCentPerMinute\": 15}]")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PutMapping("/streets/{streetName}")
    public ResponseEntity<StreetDetails> updateStreetDetails(@PathVariable String streetName, @RequestBody StreetDetails streetDetails) {
        StreetDetails response = streetService.updateStreetDetails(streetName, streetDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "removeStreetDetails",
                            description = "Delete Street Details by streetName.")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @DeleteMapping("/streets/{streetName}")
    public void removeStreetDetails(@PathVariable String streetName) {
        streetService.removeStreet(streetName);
    }
}
