package com.apps.QuantityMeasurementApp.service;

import com.apps.QuantityMeasurementApp.dto.QuantityDTO;
import com.apps.QuantityMeasurementApp.enums.LengthUnit;
import com.apps.QuantityMeasurementApp.enums.TemperatureUnit;
import com.apps.QuantityMeasurementApp.enums.VolumeUnit;
import com.apps.QuantityMeasurementApp.enums.WeightUnit;
import com.apps.QuantityMeasurementApp.model.Quantity;
import com.apps.QuantityMeasurementApp.model.Unit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Service implementation for quantity measurement operations.
 * Resolves units dynamically and validates that operations are performed
 * on quantities of the same category.
 */
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger log = LogManager.getLogger(QuantityMeasurementServiceImpl.class);

    // 🔥 Resolve unit dynamically (UC10 core concept)
    private Unit resolveUnit(String unit) {
        log.debug("Resolving unit: {}", unit);
        try { return LengthUnit.valueOf(unit); }      catch (Exception ignored) {}
        try { return WeightUnit.valueOf(unit); }      catch (Exception ignored) {}
        try { return VolumeUnit.valueOf(unit); }      catch (Exception ignored) {}
        try { return TemperatureUnit.valueOf(unit); } catch (Exception ignored) {}

        log.error("Invalid unit provided: {}", unit);
        throw new IllegalArgumentException("Invalid Unit: " + unit);
    }

    // 🔥 Ensure same category (Length != Temperature ❌)
    private void validateSameCategory(Unit u1, Unit u2) {
        if (u1.getCategory() != u2.getCategory()) {
            log.warn("Category mismatch: {} vs {}", u1.getCategory(), u2.getCategory());
            throw new IllegalArgumentException("Different unit categories not allowed");
        }
    }

    // ================= ADD =================
    @Override
    public QuantityDTO addQuantity(QuantityDTO q1, QuantityDTO q2) {
        log.debug("addQuantity: {} {} + {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);
        Quantity result = new Quantity(q1.getValue(), u1).add(new Quantity(q2.getValue(), u2));
        log.debug("addQuantity result: {} {}", result.getValue(), result.getUnit());
        return new QuantityDTO(result.getValue(), result.getUnit().toString());
    }

    // ================= COMPARE =================
    @Override
    public boolean compareQuantity(QuantityDTO q1, QuantityDTO q2) {
        log.debug("compareQuantity: {} {} vs {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        boolean result = new Quantity(q1.getValue(), u1).equals(new Quantity(q2.getValue(), u2));
        log.debug("compareQuantity result: {}", result);
        return result;
    }

    // ================= SUBTRACT =================
    @Override
    public QuantityDTO subQuantity(QuantityDTO q1, QuantityDTO q2) {
        log.debug("subQuantity: {} {} - {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);
        Quantity result = new Quantity(q1.getValue(), u1).subtract(new Quantity(q2.getValue(), u2));
        log.debug("subQuantity result: {} {}", result.getValue(), result.getUnit());
        return new QuantityDTO(result.getValue(), result.getUnit().toString());
    }

    // ================= DIVIDE =================
    @Override
    public double divQuantity(QuantityDTO q1, QuantityDTO q2) {
        log.debug("divQuantity: {} {} / {} {}", q1.getValue(), q1.getUnit(), q2.getValue(), q2.getUnit());
        Unit u1 = resolveUnit(q1.getUnit());
        Unit u2 = resolveUnit(q2.getUnit());
        validateSameCategory(u1, u2);
        double result = new Quantity(q1.getValue(), u1).divide(new Quantity(q2.getValue(), u2));
        log.debug("divQuantity result: {}", result);
        return result;
    }
}