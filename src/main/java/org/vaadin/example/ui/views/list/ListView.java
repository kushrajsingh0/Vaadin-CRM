package org.vaadin.example.ui.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.backend.entity.Company;
import org.vaadin.example.backend.entity.Contact;
import org.vaadin.example.backend.service.CompanyService;
import org.vaadin.example.backend.service.ContactService;
import org.vaadin.example.ui.MainLayout;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {


    Grid<Contact> grid = new Grid<>(Contact.class); //making a grid of contacts
    TextField filterText = new TextField(); //adding a text-field (for searching)
    private ContactForm form;


    private final ContactService contactService;


    public ListView(ContactService contactService, CompanyService companyService) {
        this.contactService = contactService; //connecting the main view to the contacts
        addClassName("list-view"); //css classname for the main view
        setSizeFull(); // main view is the browser's full size window
        //add(grid); //adding the grid to the main view
        configureGrid();
        getToolbar();

        form = new ContactForm(companyService.findAll()); //initializing a new contact form
        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid, form); //displaying the form in a html div element
        content.addClassName("content"); //adding css classname
        content.setSizeFull(); //making the form full size

        add(getToolbar(), content);
        updateList();
        closeEditor();

    }

    private void deleteContact(ContactForm.DeleteEvent evt) {
        contactService.delete((evt.getContact()));
        updateList();
        closeEditor();
    }

    private void saveContact(ContactForm.SaveEvent evt) {
        contactService.save(evt.getContact());
        updateList();
        closeEditor();
    }

    //hide form if no contact is selected
    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    //configures the grid
    private void configureGrid() {

        //css classname
        grid.addClassName("contact-grid");

        //full browser size
        grid.setSizeFull();

        //which columns to show and in which order
        grid.setColumns("firstName", "lastName", "email", "status", "company");

        // fixing the grid not showing correct company name
        grid.removeColumnByKey("company");
        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        //equal column space
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //
        grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
    }

    private void editContact(Contact contact) {
        if(contact==null) {
            closeEditor();
        }
        else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name..."); //placeholder
        filterText.setClearButtonVisible(true); //clear button is visible
        filterText.setValueChangeMode(ValueChangeMode.LAZY); //input mode will wait before changes text
        filterText.addValueChangeListener(e -> updateList()); //updating the list using the updateList method

        Button addContactButton = new Button ("Add contact", click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;


    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    // bring data to the list (filtered)
    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }

}