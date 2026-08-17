package com.example.calculator.controller;

import com.example.calculator.service.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private static final Logger log =
            LoggerFactory.getLogger(CalculatorController.class);

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/add")
    public Map<String, Object> add(@RequestParam double a, @RequestParam double b) {
        return response(a, b, "ADD", calculatorService.add(a, b));
    }

    @GetMapping("/message")
    public String msg() {
        log.info("Version 26 Message API Executed");
        return "Version 26 deployed from Jenkins Docker Pipeline";
         }

    @GetMapping("/subtract")
    public Map<String, Object> subtract(@RequestParam double a, @RequestParam double b) {
        return response(a, b, "SUBTRACT", calculatorService.subtract(a, b));
    }

    @GetMapping("/multiply")
    public Map<String, Object> multiply(@RequestParam double a, @RequestParam double b) {
        return response(a, b, "MULTIPLY", calculatorService.multiply(a, b));
    }

    @GetMapping("/divide")
    public Map<String, Object> divide(@RequestParam double a, @RequestParam double b) {
        return response(a, b, "DIVIDE", calculatorService.divide(a, b));
    }

    private Map<String, Object> response(double a, double b, String operation, double result) {
        return Map.of(
                "firstNumber", a,
                "secondNumber", b,
                "operation", operation,
                "result", result
        );
    }
}
