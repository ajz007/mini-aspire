package com.miniaspire.loan.controller.integrationtest;

import com.miniaspire.loan.LoanApplication;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = LoanApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class LoanRestControllerIntegrationTest {

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
    public void createLoanSunnyDayTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER)
                        .content(createLoanInput()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getLoanSunnyDayTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @Ignore
    public void getRepaymentsSunnyDayTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/loan/repayments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("loanAccount","1")
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private String createLoanInput() {
        return "{\n" +
                "  \"account\": \"1\",\n" +
                "  \"loanAmount\": 1,\n" +
                "  \"term\": 1\n" +
                "}";
    }

}
