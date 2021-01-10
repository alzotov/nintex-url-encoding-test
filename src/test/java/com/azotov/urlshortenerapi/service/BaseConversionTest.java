package com.azotov.urlshortenerapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaseConversionTest {

    private BaseConversion baseConversionUnderTest;

    @BeforeEach
    void setUp() {
        baseConversionUnderTest = new BaseConversion();
    }

    @Test
    void testEncode() {
        // Setup

        // Run the test
        final String result = baseConversionUnderTest.encode(0L);

        // Verify the results
        assertThat(result).isEqualTo("a");
    }

    @Test
    void testDecode() {
        // Setup

        // Run the test
        final long result = baseConversionUnderTest.decode("a");

        // Verify the results
        assertThat(result).isEqualTo(0L);
    }
}
