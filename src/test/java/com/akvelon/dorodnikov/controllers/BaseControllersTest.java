package com.akvelon.dorodnikov.controllers;

import com.akvelon.dorodnikov.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

/**
 * Base class for controller test classes.
 */
public class BaseControllersTest {

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Verifies that the JSON response matches the expected result.
     *
     * @param result object to verify.
     * @param list List of expected error messages.
     * @throws IOException if the HTTP response will be empty.
     */
    public void getListOfError(MvcResult result, List list) throws IOException {

        ErrorResponseDTO errorDTO = objectMapper.readValue(result.getResponse().getContentAsString(),
                ErrorResponseDTO.class);
        assertThat(errorDTO.getErrorDtos()).hasSameElementsAs(list);
    }
}