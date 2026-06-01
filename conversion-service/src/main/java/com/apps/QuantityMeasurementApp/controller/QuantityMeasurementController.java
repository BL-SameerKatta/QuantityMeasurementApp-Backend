package com.apps.QuantityMeasurementApp.controller;

import com.apps.QuantityMeasurementApp.dto.QuantityDTO;
import com.apps.QuantityMeasurementApp.service.IQuantityMeasurementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for quantity measurement operations.
 * All endpoints return ResponseEntity for proper HTTP status handling.
 * Exceptions are handled by {@link GlobalExceptionHandler}.
 */
@RestController
@RequestMapping("/quantity")
public class QuantityMeasurementController {

    private static final Logger log = LogManager.getLogger(QuantityMeasurementController.class);

    @Autowired
    private IQuantityMeasurementService service;

    /**
     * POST /quantity/add
     * Adds two quantities of the same unit category.
     * Returns 200 OK with the result QuantityDTO.
     */
    @PostMapping("/add")
    public ResponseEntity<QuantityDTO> add(@RequestBody QuantityDTO[] quantities) {
        log.info("POST /quantity/add — {} {} + {} {}",
                quantities[0].getValue(), quantities[0].getUnit(),
                quantities[1].getValue(), quantities[1].getUnit());
        QuantityDTO result = service.addQuantity(quantities[0], quantities[1]);
        log.debug("Add result: {} {}", result.getValue(), result.getUnit());
        return ResponseEntity.ok(result);
    }

    /**
     * POST /quantity/compare
     * Compares two quantities for equality.
     * Returns 200 OK with a boolean result.
     */
    @PostMapping("/compare")
    public ResponseEntity<Boolean> compare(@RequestBody QuantityDTO[] quantities) {
        log.info("POST /quantity/compare — {} {} vs {} {}",
                quantities[0].getValue(), quantities[0].getUnit(),
                quantities[1].getValue(), quantities[1].getUnit());
        boolean result = service.compareQuantity(quantities[0], quantities[1]);
        log.debug("Compare result: {}", result);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /quantity/subtract
     * Subtracts the second quantity from the first.
     * Returns 200 OK with the result QuantityDTO.
     */
    @PostMapping("/subtract")
    public ResponseEntity<QuantityDTO> subtract(@RequestBody QuantityDTO[] quantities) {
        log.info("POST /quantity/subtract — {} {} - {} {}",
                quantities[0].getValue(), quantities[0].getUnit(),
                quantities[1].getValue(), quantities[1].getUnit());
        QuantityDTO result = service.subQuantity(quantities[0], quantities[1]);
        log.debug("Subtract result: {} {}", result.getValue(), result.getUnit());
        return ResponseEntity.ok(result);
    }

    /**
     * POST /quantity/divide
     * Divides the first quantity by the second.
     * Returns 200 OK with the ratio as a double.
     */
    @PostMapping("/divide")
    public ResponseEntity<Double> divide(@RequestBody QuantityDTO[] quantities) {
        log.info("POST /quantity/divide — {} {} / {} {}",
                quantities[0].getValue(), quantities[0].getUnit(),
                quantities[1].getValue(), quantities[1].getUnit());
        double result = service.divQuantity(quantities[0], quantities[1]);
        log.debug("Divide result: {}", result);
        return ResponseEntity.ok(result);
    }
}