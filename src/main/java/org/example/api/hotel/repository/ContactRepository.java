package org.example.api.hotel.repository;

import org.example.api.hotel.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}