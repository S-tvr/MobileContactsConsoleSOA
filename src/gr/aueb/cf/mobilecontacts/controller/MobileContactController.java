package gr.aueb.cf.mobilecontacts.controller;

import gr.aueb.cf.mobilecontacts.core.serializer.Serializer;
import gr.aueb.cf.mobilecontacts.dao.IMobileContactDAO;
import gr.aueb.cf.mobilecontacts.dao.MobileContactDAOImpl;
import gr.aueb.cf.mobilecontacts.dto.MobileContactInsertDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactReadOnlyDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactUpdateDTO;
import gr.aueb.cf.mobilecontacts.exceptions.ContactNotFoundException;
import gr.aueb.cf.mobilecontacts.exceptions.PhoneNumberAlreadyExistsException;
import gr.aueb.cf.mobilecontacts.mapper.Mapper;
import gr.aueb.cf.mobilecontacts.model.MobileContact;
import gr.aueb.cf.mobilecontacts.serviceLayer.IMobileContactService;
import gr.aueb.cf.mobilecontacts.serviceLayer.MobileContactServiceImpl;
import gr.aueb.cf.mobilecontacts.validation.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Δεν δίνει publicAPI αλλά endpoints.
 * Συνήθως είναι είτε Στρινγ data serialized οου readOnly είτε websites.
 * Επειδή έχουμε κανει dependency injection στο service, το service
 * περιμένει να του δωθεί ένα dao.
 * o controller είναι αυτος που θα του το δώσει. (να κάνει new)
 * Ta public methods του controller ονομάζονται endpoints.
 * Το publicApi τελοσπαντων του controller.
 */
public class MobileContactController {
    private final IMobileContactDAO dao = new MobileContactDAOImpl();
    private final IMobileContactService service = new MobileContactServiceImpl(dao);

    //endpoint
    public String insertContact(MobileContactInsertDTO insertDTO) {
        MobileContact mobileContact;
        MobileContactReadOnlyDTO readOnlyDTO;

        try {
            //validate DTO
            String errors = ValidationUtil.validateDTO(insertDTO);
            if (!errors.isEmpty()) {
                return "Error. " + "Validation Error" + errors;
            }

            //if validation is ok insert.
            mobileContact = service.insertMobileContact(insertDTO);
            //mapping
            readOnlyDTO = Mapper.mapMobileContactToDTO(mobileContact);
            //serialization Json Strings ή xlm.
            return "OK.\n" + Serializer.serializeDTO(readOnlyDTO);

        } catch (PhoneNumberAlreadyExistsException e) {
            return "Error. \n" + e.getMessage() + "\n";
        }
    }


    public String updateContact(MobileContactUpdateDTO updateDTO) {
        MobileContact mobileContact;
        MobileContactReadOnlyDTO readOnlyDTO;

        try {
            //validate DTO
            String errors = ValidationUtil.validateDTO(updateDTO);
            if (!errors.isEmpty()) {
                return "Error.\n" + "Validation Error\n" + errors;
            }

            //if validation is ok insert.
            mobileContact = service.updateMobileContact(updateDTO);
            //mapping
            readOnlyDTO = Mapper.mapMobileContactToDTO(mobileContact);
            //serialization Json Strings ή xlm.
            return "OK.\n" + Serializer.serializeDTO(readOnlyDTO);

        } catch (PhoneNumberAlreadyExistsException e) {
            return "Error.\n" + "Λάθος κατά την ενημέρωση" + e.getMessage() + "\n";
        } catch (ContactNotFoundException e) {
            return "Error.\n" + e.getMessage() + "\n";
        }
    }

    public String deleteContactById(Long id) {
        try {
            service.deleteContactById(id);
            return "OK\n Η επαφή διαγράφηκε";
        } catch (ContactNotFoundException e) {
            return "Error.\n Λάθος κατα την διαγραφή\nΗ επαφή δεν βρέθηκε.";
        }
    }

    public String getContactById(long id) {
        MobileContact mobileContact;
        MobileContactReadOnlyDTO readOnlyDTO;
        try {
            mobileContact = service.getContactById(id);
            readOnlyDTO = Mapper.mapMobileContactToDTO(mobileContact);
            return "Ok\nΒρέθηκε και είναι ο εξής\n" + Serializer.serializeDTO(readOnlyDTO);
        } catch (ContactNotFoundException e) {
            return "Error.\n Η επαφή δεν βρέθηκε.\n";
        }
    }

    public List<String> getAllContacts() {
        List<MobileContact> contacts;
        List<String> serializedList = new ArrayList<>();
        MobileContactReadOnlyDTO readOnlyDTO;
        String serialized;

        contacts = service.getAllContacts();
        for (MobileContact contact : contacts) {
            readOnlyDTO = Mapper.mapMobileContactToDTO(contact);
            serialized = Serializer.serializeDTO(readOnlyDTO);
            serializedList.add(serialized);
        }
        return serializedList;
    }

    public String getContactByPhoneNumber(String phoneNumber) {
        MobileContact mobileContact;
        MobileContactReadOnlyDTO readOnlyDTO;
        try {
            mobileContact = service.getContactByPhoneNumber(phoneNumber);
            readOnlyDTO = Mapper.mapMobileContactToDTO(mobileContact);
            return "Ok\nΒρέθηκε και είναι ο εξής\n" + Serializer.serializeDTO(readOnlyDTO);
        } catch (ContactNotFoundException e) {
            return "Error.\n Η επαφή δεν βρέθηκε.\n";
        }
    }

    public String deleteContactByPhoneNumber(String phoneNumber) {
        MobileContact mobileContact;
        MobileContactReadOnlyDTO readOnlyDTO;
        try {
            mobileContact = service.getContactByPhoneNumber(phoneNumber);
            readOnlyDTO = Mapper.mapMobileContactToDTO(mobileContact);

            service.deleteContactByPhoneNumber(phoneNumber);
            return "OK\n Η επαφή διαγράφηκε: " + Serializer.serializeDTO(readOnlyDTO);
        } catch (ContactNotFoundException e) {
            return "Error.\n Λάθος κατά την διαγραφή\nΗ επαφή δεν βρέθηκε.";
        }
    }
}
