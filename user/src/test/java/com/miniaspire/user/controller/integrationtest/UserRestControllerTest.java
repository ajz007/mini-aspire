package com.miniaspire.user.controller.integrationtest;

import com.miniaspire.user.UserApplication;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = UserApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class UserRestControllerTest {

    private static final String USER_NAME_HEADER = "x-user_name";
    private static final String USER_ROLE_HEADER = "x-user_role";

    //data for customer
    private static String USER_NAME_CUSTOMER = "testuser";
    private static final String USER_PASS = "pass";
    private static final String USER_ROLE_CUSTOMER = "USER";

    //data for admin
    private static final String USER_NAME_ADMIN = "admin";
    private static final String USER_ROLE_ADMIN = "ADMIN";

    private static int i;

    @Autowired
    private MockMvc mvc;

    @Before
    public void before() {
        USER_NAME_CUSTOMER += i++;
    }

    @Test
    public void getUserSunnyDay() throws Exception {
        createUser(USER_NAME_CUSTOMER, USER_NAME_CUSTOMER, USER_PASS, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.get("/user/" + USER_NAME_CUSTOMER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getUserInvalidId() throws Exception {
        createUser(USER_NAME_CUSTOMER, USER_NAME_CUSTOMER, USER_PASS, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.get("/user/" + "Invalid_LOGIN_ID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void getUsersSunnyDay() throws Exception {
        createUser(USER_NAME_CUSTOMER, USER_NAME_CUSTOMER, USER_PASS, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.get("/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void registerUserSunnyDay() throws Exception {
        createUser(USER_NAME_CUSTOMER, USER_NAME_CUSTOMER, USER_PASS, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void registerUserInvalidRoleThrowsException() throws Exception {
        createUser(USER_NAME_CUSTOMER, USER_NAME_CUSTOMER, USER_PASS, "INVALID_ROLE")
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    public void validateUserSunnyDay() throws Exception {
        createUser(USER_NAME_CUSTOMER, USER_NAME_CUSTOMER, USER_PASS, USER_ROLE_CUSTOMER)
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mvc.perform(MockMvcRequestBuilders.post("/user/validate")
                .content(createUserValidateRequest(USER_NAME_CUSTOMER,USER_PASS))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    private String createLoanInput(String username, String loginId, String pass, String role) {
        return "{\n" +
                "  \"username\": \"" + username + "\",\n" +
                "  \"loginId\": \"" + loginId + "\",\n" +
                "  \"password\": \"" + pass + "\",\n" +
                "  \"email\": \"default.com\",\n" +
                "  \"userRole\": \"" + role + "\"\n" +
                "}";
    }

    private String createUserValidateRequest(String username, String pass) {
        return "{\n" +
                "  \"username\": \""+username+"\",\n" +
                "  \"password\": \""+pass+"\"\n" +
                "}";
    }


    private ResultActions createUser(String username, String loginId, String pass, String role) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/user/register")
                .content(createLoanInput(username, loginId, pass, role))
                .contentType(MediaType.APPLICATION_JSON));
    }
}
