package com.rtd.finance_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,com.itextpdf.html2pdf.HtmlConverter"
})
class BasicApplicationTest {

    @Test
    void basicContextLoads() {
        // This test will verify that the basic application context loads successfully
        System.out.println("Basic application context loaded successfully!");
    }

}
