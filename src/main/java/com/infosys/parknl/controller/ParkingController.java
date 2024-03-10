package com.infosys.parknl.controller;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.model.RegisterDTO;
import com.infosys.parknl.model.UnRegisterDTO;
import com.infosys.parknl.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ParkNLConstants.API_VERSION_V1)
public class ParkingController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "register",
                            description = "Register a car when parking in the street.",
                            value = "{\"message\": \"PARKING HAS STARTED AT 2024-03-10T18:48:10 \"}")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO parkingDetails) {
        String response = parkingService.register(parkingDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "register",
                            description = "UnRegister a car before removing the car from the street parking.",
                            value = "{\"message\": \"PARKING SESSION HAS ENDED AT 2024-03-10T18:48:10 \"}")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping("/unregister")
    public ResponseEntity<String> unregister(@RequestBody UnRegisterDTO unregisterDTO) {
        String response = parkingService.unregister(unregisterDTO);
        return ResponseEntity.ok(response);
    }

}
