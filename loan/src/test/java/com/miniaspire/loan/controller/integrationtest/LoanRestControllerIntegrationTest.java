package com.miniaspire.loan.controller.integrationtest;

import com.miniaspire.loan.LoanApplication;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
                        .content(createLoanInput("10001", "10000", "3")))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createLoanAdminThrowsException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_ADMIN)
                        .header(USER_ROLE_HEADER, USER_ROLE_ADMIN)
                        .content(createLoanInput("10001", "10000", "3")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

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
    public void getRepaymentsSunnyDayTest() throws Exception {

        createLoanAccount("10002", "10000", "3");

        mvc.perform(MockMvcRequestBuilders.get("/loan/repayments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("loanAccount","10002")
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getRepaymentsValidateNumberOfRepayments() throws Exception {

        createLoanAccount("10002", "10000", "3");

        mvc.perform(MockMvcRequestBuilders.get("/loan/repayments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("loanAccount","10002")
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));
    }

    private String createLoanInput(String account, String amount, String term) {
        return "{\n" +
                "  \"account\": "+account+",\n" +
                "  \"loanAmount\": "+amount+",\n" +
                "  \"term\": "+term+"\n" +
                "}";
    }

    private void createLoanAccount(String account, String amount, String term) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER)
                        .content(createLoanInput(account, amount, term)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
