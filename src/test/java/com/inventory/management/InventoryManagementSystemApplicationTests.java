
package com.inventory.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class InventoryManagementSystemApplicationTests {


    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
        // You can even verify the mock later if needed
    }
    
}



//package com.inventory.management;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//
//@SpringBootTest
//@Configuration
//@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
//class InventoryManagementSystemApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
//}
