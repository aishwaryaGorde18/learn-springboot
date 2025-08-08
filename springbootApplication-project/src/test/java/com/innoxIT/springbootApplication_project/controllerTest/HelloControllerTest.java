package com.innoxIT.springbootApplication_project.controllerTest;

import com.innoxIT.springbootApplication_project.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Aishwarya"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Aishwarya!"));
    }

    @Test
    void testSayHelloMissingParam() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isBadRequest()); // since @RequestParam is required
    }
}
