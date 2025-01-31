package gr.aueb.cf.mobilecontacts.controller;

import gr.aueb.cf.mobilecontacts.dao.IMobileContactDAO;
import gr.aueb.cf.mobilecontacts.dao.MobileContactDAOImpl;
import gr.aueb.cf.mobilecontacts.dto.MobileContactInsertDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactReadOnlyDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactUpdateDTO;
import gr.aueb.cf.mobilecontacts.exceptions.PhoneNumberAlreadyExistsException;
import gr.aueb.cf.mobilecontacts.model.MobileContact;
import gr.aueb.cf.mobilecontacts.serviceLayer.IMobileContactService;
import gr.aueb.cf.mobilecontacts.serviceLayer.MobileContactServiceImpl;
import gr.aueb.cf.mobilecontacts.validation.ValidationUtil;

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
            readOnlyDTO = mapMobileContactToDTO(mobileContact);
            //serialization Json Strings ή xlm.
            return "OK.\n" + serializeDTO(readOnlyDTO);

        } catch (PhoneNumberAlreadyExistsException e) {
            return "Error. \n" + e.getMessage() + "\n";
        }
    }

    private MobileContactReadOnlyDTO mapMobileContactToDTO (MobileContact mobileContact) {
        return new MobileContactReadOnlyDTO(mobileContact.getId(), mobileContact.getFirstname(),
                mobileContact.getLastname(), mobileContact.getPhoneNumber());
    }

    private String serializeDTO(MobileContactReadOnlyDTO readOnlyDTO) {
        return "ID: " + readOnlyDTO.getId() + ", Όνομα: " + readOnlyDTO.getFirstname() +
                "Επίθετο: " + readOnlyDTO.getLastname() + "Τηλ.: " + readOnlyDTO.getPhoneNumber();
    }

}
