package com.concord.trivio.controller;

import com.concord.trivio.entity.Employee;
import com.concord.trivio.repository.ClientRepository;
import com.concord.trivio.repository.ContractEquipmentRepository;
import com.concord.trivio.repository.ContractRepository;
import com.concord.trivio.repository.ContractRequirementRepository;
import com.concord.trivio.repository.EmployeeRepository;
import com.concord.trivio.repository.EquipmentRepository;
import com.concord.trivio.repository.MaintenanceEmployeeRepository;
import com.concord.trivio.repository.MaintenanceRepository;
import com.concord.trivio.repository.RequirementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "app.security.jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci11bml0LXRlc3Rpbmctb25seS10aGlzLWlzLWxvbmctZW5vdWdo",
    "app.security.jwt.expiration-seconds=3600",
    "spring.autoconfigure.exclude=" +
        "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
        "org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration," +
        "org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration"
})
class AuthControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired PasswordEncoder passwordEncoder;

    @MockitoBean EmployeeRepository employeeRepository;
    @MockitoBean ClientRepository clientRepository;
    @MockitoBean ContractRepository contractRepository;
    @MockitoBean ContractEquipmentRepository contractEquipmentRepository;
    @MockitoBean ContractRequirementRepository contractRequirementRepository;
    @MockitoBean EquipmentRepository equipmentRepository;
    @MockitoBean MaintenanceRepository maintenanceRepository;
    @MockitoBean MaintenanceEmployeeRepository maintenanceEmployeeRepository;
    @MockitoBean RequirementRepository requirementRepository;

    @Test
    void login_validManagerCredentials_returns200WithTokenAndRole() throws Exception {
        Employee emp = new Employee(1L, "Ana Gestora", true, true, "ana@trivio.com",
            passwordEncoder.encode("senha123"));

        when(employeeRepository.findByEmail("ana@trivio.com")).thenReturn(Optional.of(emp));

        String body = objectMapper.writeValueAsString(
            Map.of("email", "ana@trivio.com", "password", "senha123")
        );

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.role").value("MANAGER"))
            .andExpect(jsonPath("$.email").value("ana@trivio.com"))
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void login_validTechnicianCredentials_returns200WithTechnicianRole() throws Exception {
        Employee emp = new Employee(2L, "Carlos Tecnico", false, true, "carlos@trivio.com",
            passwordEncoder.encode("senha456"));

        when(employeeRepository.findByEmail("carlos@trivio.com")).thenReturn(Optional.of(emp));

        String body = objectMapper.writeValueAsString(
            Map.of("email", "carlos@trivio.com", "password", "senha456")
        );

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.role").value("TECHNICIAN"));
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        Employee emp = new Employee(1L, "Ana", true, true, "ana@trivio.com",
            passwordEncoder.encode("senha123"));

        when(employeeRepository.findByEmail("ana@trivio.com")).thenReturn(Optional.of(emp));

        String body = objectMapper.writeValueAsString(
            Map.of("email", "ana@trivio.com", "password", "senhaErrada")
        );

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_unknownEmail_returns401() throws Exception {
        when(employeeRepository.findByEmail("ninguem@trivio.com")).thenReturn(Optional.empty());

        String body = objectMapper.writeValueAsString(
            Map.of("email", "ninguem@trivio.com", "password", "qualquer")
        );

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void login_missingEmail_returns400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("password", "senha123"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }
}
