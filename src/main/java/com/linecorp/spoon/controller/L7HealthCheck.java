package com.linecorp.spoon.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class L7HealthCheck {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> healthCheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
