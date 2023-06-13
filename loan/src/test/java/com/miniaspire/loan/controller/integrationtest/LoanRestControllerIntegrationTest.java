package com.miniaspire.loan.controller.integrationtest;

import com.jayway.jsonpath.JsonPath;
import com.miniaspire.loan.LoanApplication;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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

    //data for admin
    private static final String USER_NAME_ADMIN = "admin";
    private static final String USER_ROLE_ADMIN = "ADMIN";

    //Account
    private static Integer LOAN_ACCOUNT = 1000;

    @Autowired
    private MockMvc mvc;

    @Before
    public void before() {
        LOAN_ACCOUNT++;
    }

    @Test
    public void createLoanSunnyDayTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER)
                        .content(createLoanInput(LOAN_ACCOUNT.toString(), "10000", "3")))
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
                        .content(createLoanInput(LOAN_ACCOUNT.toString(), "10000", "3")))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    public void getLoansSunnyDayTest() throws Exception {
        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");
        LOAN_ACCOUNT++;
        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");
        LOAN_ACCOUNT++;
        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        executeLoanInquiry(USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("PENDING")));

        executeLoanInquiry(USER_NAME_ADMIN, USER_ROLE_ADMIN)
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("PENDING")));
    }

    @Test
    public void getLoanSunnyDayTest() throws Exception {
        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");
        executeLoanInquiry(LOAN_ACCOUNT.toString(), USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("PENDING")));

        executeLoanInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("PENDING")));
    }

    @Test
    public void getRepaymentsSunnyDayTest() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER, "")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN, "")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN, "?repaymentStatus=PENDING")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getRepaymentsUnAuthorisedThrowsException() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), "", "", "")
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    public void getRepaymentsValidateNumberOfRepayments() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER, "")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN, "")
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateLoanSunnyDay() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        //update status of loan to APPROVED
        mvc.perform(MockMvcRequestBuilders.put("/loan/" + LOAN_ACCOUNT)
                        .param("loanStatus", "APPROVED")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_ADMIN)
                        .header(USER_ROLE_HEADER, USER_ROLE_ADMIN))
                .andExpect(MockMvcResultMatchers.status().isOk());

        executeLoanInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("APPROVED")));
    }

    @Test
    public void updateLoanInvalidStatusThrowsException() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        //update status of loan to APPROVED
        mvc.perform(MockMvcRequestBuilders.put("/loan/" + LOAN_ACCOUNT)
                        .param("loanStatus", "INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_ADMIN)
                        .header(USER_ROLE_HEADER, USER_ROLE_ADMIN))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    public void updateLoanStatusInvalidUserRoleThrowsException() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        //update status of loan to APPROVED
        mvc.perform(MockMvcRequestBuilders.put("/loan/" + LOAN_ACCOUNT)
                        .param("loanStatus", "APPROVED")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        executeLoanInquiry(LOAN_ACCOUNT.toString(), USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("PENDING")));
    }

    @Test
    public void updateRepaymentsSunnyDay() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        var mvcResult = executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN, "")
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");
        //update status of loan to APPROVED
        mvc.perform(MockMvcRequestBuilders.put("/loan/repayments/" + 1)
                        .content(createRepaymentInput(id, "10000", "PAID"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_ADMIN)
                        .header(USER_ROLE_HEADER, USER_ROLE_ADMIN))
                .andExpect(MockMvcResultMatchers.status().isOk());

        executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_ADMIN, USER_ROLE_ADMIN, "?repaymentStatus=PAID")
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("PAID")));
    }

    @Test
    public void updateRepaymentsUserRoleThrowsException() throws Exception {

        createLoanAccount(LOAN_ACCOUNT.toString(), "10000", "3");

        var mvcResult = executeRepaymentInquiry(LOAN_ACCOUNT.toString(), USER_NAME_CUSTOMER, USER_ROLE_CUSTOMER, "")
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");

        mvc.perform(MockMvcRequestBuilders.put("/loan/repayments/" + 1)
                        .content(createRepaymentInput(id, "10000", "PAID"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, USER_NAME_CUSTOMER)
                        .header(USER_ROLE_HEADER, USER_ROLE_CUSTOMER))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    private String createLoanInput(String account, String amount, String term) {
        return "{\n" +
                "  \"account\": " + account + ",\n" +
                "  \"loanAmount\": " + amount + ",\n" +
                "  \"term\": " + term + "\n" +
                "}";
    }

    private String createRepaymentInput(Integer id, String amount, String status) {
        return "{" +
                "  \"id\":" + id + "," +
                "  \"amount\": " + amount + "," +
                "  \"dueDate\": \"2023-06-12\"," +
                "  \"status\": \"" + status + "\"," +
                "  \"createdDate\": \"2023-06-12T19:28:41.245Z\"" +
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

    private ResultActions executeLoanInquiry(String loanAccount, String username, String userRole) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/loan/" + loanAccount)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, username)
                        .header(USER_ROLE_HEADER, userRole))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private ResultActions executeLoanInquiry(String username, String userRole) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_NAME_HEADER, username)
                        .header(USER_ROLE_HEADER, userRole))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private ResultActions executeRepaymentInquiry(String loanAccount, String username, String role, String queryParam) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/loan/repayments/" + loanAccount + queryParam)
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_NAME_HEADER, username)
                .header(USER_ROLE_HEADER, role));
    }

}
