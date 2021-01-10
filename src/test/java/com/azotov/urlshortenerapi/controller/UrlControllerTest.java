package com.azotov.urlshortenerapi.controller;

import com.azotov.urlshortenerapi.dto.UrlLongRequest;
import com.azotov.urlshortenerapi.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService mockUrlService;

    @Test
    void testConvertToShortUrl() throws Exception {
        // Setup
        when(mockUrlService.convertToShortUrl(any(UrlLongRequest.class))).thenReturn("shortURL");

        UrlLongRequest rq = new UrlLongRequest();
        rq.setLongUrl("longURL");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rq);
        System.out.println("Test Data = "+json);
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/create-short")
                //.content("{ 'longUrl': 'longURL' }").contentType(MediaType.APPLICATION_JSON)
                //.content(rq).contentType(MediaType.APPLICATION_JSON)
                .content(json).contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("shortURL");
    }

    @Test
    void testGetAndRedirect() throws Exception {
        // Setup
        when(mockUrlService.getOriginalUrl("shortURL")).thenReturn("longURL");

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/{shortUrl}", "shortURL")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getHeader("Location").compareTo("longURL") == 0);
    }
}
