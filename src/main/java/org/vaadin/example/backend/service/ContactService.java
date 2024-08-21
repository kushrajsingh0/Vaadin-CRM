package org.vaadin.example.backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.Company;
import org.vaadin.example.backend.entity.Contact;
import org.vaadin.example.backend.repository.CompanyRepository;
import org.vaadin.example.backend.repository.ContactRepository;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//backend copied, code
@Service
public class ContactService {
    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private ContactRepository contactRepository;
    private CompanyRepository companyRepository;

    public ContactService(ContactRepository contactRepository,
                          CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public List<Contact> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(filterText);
        }
    }

    public long count() {
        return contactRepository.count();
    }

    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }

    public void save(Contact contact) {
        if (contact == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    @PostConstruct
    public void populateTestData() {
        if (companyRepository.count() == 0) {
            companyRepository.saveAll(
                    Stream.of("Make My Trip", "Red Bus", "Bitla Software")
                            .map(Company::new)
                            .collect(Collectors.toList()));
        }

        if (contactRepository.count() == 0) {
            Random r = new Random(0);
            List<Company> companies = companyRepository.findAll();
            contactRepository.saveAll(
                    Stream.of("Kush Raj", "Raj Singh", "Ram Singh",
                                    "Vishal Patel", "Mohan Kumar", "Naveen Kumar", "Ananth Kumar", "Supriya Sen",
                                    "Virat Kohli", "Sourabh Rajput", "Laxman Mittal", "Kusum Singh", "Siva Krishna",
                                    "Sunil Kumar", "Harbhajan Jain", "Israel Roy", "Quinn Dhavan", "Smith Karn",
                                    "Danielle Jain", "Harris Das", "Karlsen Pratap", "Olsson Kumar", "Manu Bhaker",
                                    "Suramya Singh", "Elizabeth Queen", "Smith Kumar", "Elix Prabhat", "Dainik Bhaskar",
                                    "Nilsen Aman", "Nirma Patel")
                            .map(name -> {
                                String[] split = name.split(" ");
                                Contact contact = new Contact();
                                contact.setFirstName(split[0]);
                                contact.setLastName(split[1]);
                                contact.setCompany(companies.get(r.nextInt(companies.size())));
                                contact.setStatus(Contact.Status.values()[r.nextInt(Contact.Status.values().length)]);
                                String email = (contact.getFirstName() + "." + contact.getLastName() + "@" + contact.getCompany().getName().replaceAll("[\\s-]", "") + ".com").toLowerCase();
                                contact.setEmail(email);
                                return contact;
                            }).collect(Collectors.toList()));
        }
    }
}
