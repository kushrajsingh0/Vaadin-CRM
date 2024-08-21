package org.vaadin.example.backend.service;

import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.Company;
import org.vaadin.example.backend.repository.CompanyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//backend copied, code
@Service
public class CompanyService {

    private static CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public static List<Company> findAll() {
        return companyRepository.findAll();
    }

    public static Map<String, Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        findAll().forEach(company ->
                stats.put(company.getName(), company.getEmployees().size()));
        return stats;
    }

}