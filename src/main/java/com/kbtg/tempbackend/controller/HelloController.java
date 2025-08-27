package com.kbtg.tempbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Hello", description = "Hello World API")
public class HelloController {

    @GetMapping("/get")
    @Operation(summary = "Get Hello World message", description = "Returns a simple Hello World message in JSON format")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved message")
    })
    public Map<String, String> sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World");
        return response;
    }
}
