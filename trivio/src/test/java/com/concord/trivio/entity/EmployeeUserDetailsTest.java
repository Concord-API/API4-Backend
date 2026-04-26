package com.concord.trivio.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeUserDetailsTest {

    @Test
    void admin_employee_has_MANAGER_authority() {
        Employee emp = new Employee(1L, "Ana", true, true, "ana@trivio.com", "hash");
        assertThat(emp.getAuthorities())
            .map(a -> a.getAuthority())
            .containsExactly("ROLE_MANAGER");
    }

    @Test
    void non_admin_employee_has_TECHNICIAN_authority() {
        Employee emp = new Employee(2L, "Carlos", false, true, "carlos@trivio.com", "hash");
        assertThat(emp.getAuthorities())
            .map(a -> a.getAuthority())
            .containsExactly("ROLE_TECHNICIAN");
    }

    @Test
    void getUsername_returns_email() {
        Employee emp = new Employee(1L, "Ana", true, true, "ana@trivio.com", "hash");
        assertThat(emp.getUsername()).isEqualTo("ana@trivio.com");
    }

    @Test
    void isEnabled_matches_active_field() {
        Employee active = new Employee(1L, "Ana", true, true, "ana@trivio.com", "hash");
        Employee inactive = new Employee(2L, "Bob", false, false, "bob@trivio.com", "hash");
        assertThat(active.isEnabled()).isTrue();
        assertThat(inactive.isEnabled()).isFalse();
    }
}
