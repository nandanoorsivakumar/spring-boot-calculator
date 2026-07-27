package com.example.calculator.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorServiceTest {

    private final CalculatorService calculatorService = new CalculatorService();

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(15.0, calculatorService.add(10, 5));
    }

    @Test
    void shouldDivideTwoNumbers() {
        assertEquals(2.0, calculatorService.divide(10, 5));
    }

    @Test
    void shouldRejectDivisionByZero() {
        assertThrows(IllegalArgumentException.class,
                () -> calculatorService.divide(10, 0));
    }
}
