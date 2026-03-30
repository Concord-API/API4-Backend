package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concord.trivio.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
