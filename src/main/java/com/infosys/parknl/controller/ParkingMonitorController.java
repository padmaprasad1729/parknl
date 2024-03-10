package com.infosys.parknl.controller;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.model.ParkingMonitorData;
import com.infosys.parknl.service.ParkingMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ParkNLConstants.API_VERSION_V1)
public class ParkingMonitorController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ParkingMonitorService parkingMonitorService;

    public ParkingMonitorController(ParkingMonitorService parkingMonitorService) {
        this.parkingMonitorService = parkingMonitorService;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(examples = {
                    @ExampleObject(name = "addMonitoredData",
                            description = "Traffic police monitors the parked cars and send this data.")
            }, mediaType = MediaType.APPLICATION_JSON_VALUE))})
    @PostMapping("/monitored-data")
    public void addMonitoredData(@RequestBody List<ParkingMonitorData> parkingMonitorDataList) {
        parkingMonitorService.addParkingMonitorData(parkingMonitorDataList);
    }
}
