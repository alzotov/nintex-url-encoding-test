package com.azotov.urlshortenerapi.service;

import com.azotov.urlshortenerapi.dto.UrlLongRequest;
import com.azotov.urlshortenerapi.entity.Url;
import com.azotov.urlshortenerapi.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UrlServiceTest {

    @Mock
    private UrlRepository mockUrlRepository;
    @Mock
    private BaseConversion mockBaseConversion;

    private UrlService urlServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        urlServiceUnderTest = new UrlService(mockUrlRepository, mockBaseConversion);
    }

    @Test
    void testConvertToShortUrl() {
        // Setup
        final UrlLongRequest request = new UrlLongRequest();
        request.setLongUrl("longURL");

        // Configure UrlRepository.save(...).
        final Url url = new Url();
        url.setId(0L);
        url.setLongUrl("longURL");
        url.setCreatedDate(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime());
        url.setExpiresDate(new GregorianCalendar(2022, Calendar.JANUARY, 1).getTime());
        when(mockUrlRepository.save(any(Url.class))).thenReturn(url);

        when(mockBaseConversion.encode(0L)).thenReturn("a");

        // Run the test
        final String result = urlServiceUnderTest.convertToShortUrl(request);

        // Verify the results
        assertThat(result).isEqualTo("a");
    }

    @Test
    void testGetOriginalUrl() {
        // Setup
        when(mockBaseConversion.decode("a")).thenReturn(0L);

        // Configure UrlRepository.findById(...).
        final Url url1 = new Url();
        url1.setId(0L);
        url1.setLongUrl("longURL");
        url1.setCreatedDate(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime());
        url1.setExpiresDate(new GregorianCalendar(2022, Calendar.JANUARY, 1).getTime());
        final Optional<Url> url = Optional.of(url1);
        when(mockUrlRepository.findById(0L)).thenReturn(url);

        // Run the test
        final String result = urlServiceUnderTest.getOriginalUrl("a");

        // Verify the results
        assertThat(result).isEqualTo("longURL");
    }
}
