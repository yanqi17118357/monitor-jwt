package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MonitorServerApplicationTests {

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void contextLoads() {
        String rawPassword = "00000000";
        System.out.println(encoder.encode(rawPassword));
    }
}
