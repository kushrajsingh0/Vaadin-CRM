package org.vaadin.example.backend.repository;

import org.vaadin.example.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> { //database operations (Save, Edit, Delete)
}