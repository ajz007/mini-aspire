package com.miniaspire.payment.controller.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniaspire.payment.PaymentApplication;
import com.miniaspire.payment.dto.PaymentRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2SmileEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PaymentApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class PaymentRestControllerTest {

    private static final String USER_NAME_HEADER = "x-user_name";
    private static final String USER_ROLE_HEADER = "x-user_role";

    //data for customer
    private static final String USER_NAME_CUSTOMER = "testuser";
    private static final String USER_ROLE_CUSTOMER = "USER";

    //data for adin
    private static final String USER_NAME_ADMIN = "admin";
    private static final String USER_ROLE_ADMIN = "ADMIN";

    @Autowired
    private MockMvc mvc;

    @Test
    @Ignore
    public void createPaymentSunnyDay() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER)
                .content(createPaymentRequest("10001", BigDecimal.valueOf(3333))))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private String createPaymentRequest(String loanAccount, BigDecimal amount) throws JsonProcessingException {
        ObjectMapper objmap = new ObjectMapper();
        var request = new PaymentRequest(amount, loanAccount, null, "");
        return objmap.writeValueAsString(request);
    }

}
