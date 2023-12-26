package com.ivanzkyanto.tactment.repository;

import com.ivanzkyanto.tactment.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, String> {
}
