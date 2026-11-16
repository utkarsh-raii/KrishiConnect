package com.krishiconnect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KrishiConnectIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.admin.email:admin@fhc.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@1234}")
    private String adminPassword;

    private String obtainToken(String email, String password) throws Exception {
        Map<String, String> creds = Map.of("email", email, "password", password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.has("token") ? json.get("token").asText() : json.path("data").path("token").asText();
    }

    @Test
    @DisplayName("1. Health check returns 200 UP")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service", containsString("KrishiConnect")));
    }

    @Test
    @DisplayName("2. Admin login returns JWT token and admin role")
    void testAdminLogin() throws Exception {
        Map<String, String> creds = Map.of(
                "email", adminEmail != null ? adminEmail : "admin@fhc.com",
                "password", adminPassword != null ? adminPassword : "Admin@1234"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.role").value("admin"));
    }

    @Test
    @DisplayName("3. Demo Farmer login returns 200 OK and farmer role")
    void testFarmerLogin() throws Exception {
        Map<String, String> creds = Map.of(
                "email", "farmer@demo.com",
                "password", "demo123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.role").value("farmer"))
                .andExpect(jsonPath("$.user.email").value("farmer@demo.com"));
    }

    @Test
    @DisplayName("4. Demo Buyer login returns 200 OK and buyer role")
    void testBuyerLogin() throws Exception {
        Map<String, String> creds = Map.of(
                "email", "priya@buyer.com",
                "password", "Buyer@123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.role").value("buyer"))
                .andExpect(jsonPath("$.user.email").value("priya@buyer.com"));
    }

    @Test
    @DisplayName("5. Demo Provider login returns 200 OK and provider role")
    void testProviderLogin() throws Exception {
        Map<String, String> creds = Map.of(
                "email", "provider@demo.com",
                "password", "demo123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.role").value("provider"))
                .andExpect(jsonPath("$.user.email").value("provider@demo.com"));
    }

    @Test
    @DisplayName("6. Login with invalid password returns 401 Unauthorized")
    void testInvalidCredentials() throws Exception {
        Map<String, String> creds = Map.of(
                "email", "farmer@demo.com",
                "password", "wrongpassword123"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creds)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("7. Public Tree catalog returns 200 OK with seeded trees")
    void testGetPublicTrees() throws Exception {
        mockMvc.perform(get("/api/trees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.trees", hasSize(greaterThanOrEqualTo(8))))
                .andExpect(jsonPath("$.trees[?(@.name == 'Neem Tree')]").exists())
                .andExpect(jsonPath("$.trees[?(@.name == 'Banyan Tree')]").exists());
    }

    @Test
    @DisplayName("8. Public Buyer crop marketplace returns 200 OK with seeded crops")
    void testGetPublicCrops() throws Exception {
        mockMvc.perform(get("/api/buyer/listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("9. Authenticated Farmer can retrieve their crop listings")
    void testAuthenticatedFarmerGetCrops() throws Exception {
        String token = obtainToken("farmer@demo.com", "demo123");

        mockMvc.perform(get("/api/farmer/crops")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.crops", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @DisplayName("10. Authenticated Provider can retrieve their service listings")
    void testAuthenticatedProviderGetServices() throws Exception {
        String token = obtainToken("provider@demo.com", "demo123");

        mockMvc.perform(get("/api/provider/services")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.services", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @DisplayName("11. Unauthenticated request to protected endpoint returns 401 or 403")
    void testUnauthenticatedAccessFails() throws Exception {
        mockMvc.perform(get("/api/farmer/crops"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("12. Role-based authorization: Buyer accessing Farmer endpoint returns 403 Forbidden")
    void testRoleBasedAccessDenied() throws Exception {
        String buyerToken = obtainToken("priya@buyer.com", "Buyer@123");

        mockMvc.perform(get("/api/farmer/crops")
                        .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());
    }
}
