package com.rtd.finance_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
class FinanceBackendApplicationTests {

    @Test
    void contextLoads() {
        // This test will verify that the application context loads successfully
    }

}
