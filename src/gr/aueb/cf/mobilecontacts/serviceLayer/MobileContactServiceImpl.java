package gr.aueb.cf.mobilecontacts.serviceLayer;

import gr.aueb.cf.mobilecontacts.dao.IMobileContactDAO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactInsertDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactUpdateDTO;
import gr.aueb.cf.mobilecontacts.exceptions.ContactNotFoundException;
import gr.aueb.cf.mobilecontacts.exceptions.PhoneNumberAlreadyExistsException;
import gr.aueb.cf.mobilecontacts.mapper.Mapper;
import gr.aueb.cf.mobilecontacts.model.MobileContact;

import java.util.List;

/**
 * What is Constructor Dependency Injection?
 * Constructor Dependency Injection (CDI) is a technique in which dependencies
 * (objects that a class depends on) are injected through the constructor
 * rather than being created inside the class.
 * This promotes loose coupling, making the code more testable and maintainable.
 * How Constructor Dependency Injection Works
 * Instead of creating dependencies inside a class using new, we pass them
 * through the constructor. This allows external control (e.g., through a
 * framework like Spring) to manage dependencies.
 * .
 * What is Constructor Dependency Injection with Interfaces?
 * Constructor Dependency Injection (CDI) with interfaces is a technique where
 * dependencies are injected through a constructor, but instead of using a
 * concrete class, we inject an interface. This approach provides loose coupling,
 * making the code more flexible and easy to test.
 */
public class MobileContactServiceImpl implements IMobileContactService{
    // χρειάζομαι ενα private instance tou DAO.
    // Xρησιμοποιώ dependancy injection.
    // Aρα θέλω private field interface και να κανω inject μεσω του constructor.

    private final IMobileContactDAO dao;

    public MobileContactServiceImpl(IMobileContactDAO dao) {
        this.dao = dao;
    }

    @Override
    public MobileContact insertMobileContact(MobileContactInsertDTO dto)
            throws PhoneNumberAlreadyExistsException {
        MobileContact mobileContact;

        try {
            if (dao.phoneNumberExists(dto.getPhoneNumber())) {
                throw new PhoneNumberAlreadyExistsException("Contact with number:"
                + dto.getPhoneNumber() + " already exists.");
            }
            mobileContact = Mapper.mapInsertDTOToMobileContact(dto);
            //καλο είναι σε χρήση crud να γίνεται
            //καταγραφή σε log file (εστω err επειδη δεν έχω log file)
            System.err.printf("MobileContactServiceImpl logger: %s was insert\n", mobileContact );
            return dao.insert(mobileContact);
        } catch (PhoneNumberAlreadyExistsException e) {
            System.err.printf("MobileContactServiceImpl logger: Contact with phone number: %s already exists", dto.getPhoneNumber());
            throw e;
        }
    }

    @Override
    public MobileContact updateMobileContact(MobileContactUpdateDTO dto)
            throws PhoneNumberAlreadyExistsException, ContactNotFoundException {
        MobileContact mobileContact;
        MobileContact newContact;

        try {
            if (!dao.userIdExists(dto.getId())) {
                throw new ContactNotFoundException("Contact with id: " + dto.getId() + " not found for update.");
            }

            mobileContact = dao.getById(dto.getId());
            boolean isPhoneNumberOurOwn = mobileContact.getPhoneNumber().equals(dto.getPhoneNumber());
            boolean isPhoneNumberExists = dao.phoneNumberExists(dto.getPhoneNumber());

            if (isPhoneNumberExists && !isPhoneNumberOurOwn) {
                throw new PhoneNumberAlreadyExistsException("Contact with phone number: " + dto.getPhoneNumber() + "exists and cannot be updated.");
            }

            //mapping
            newContact = Mapper.mapUpdateDTOToMobileContact(dto);
            System.err.printf("MobileContactServiceImpl logger: %s was updated to: %s", mobileContact, newContact);
            return dao.update(dto.getId(), newContact);
        } catch (ContactNotFoundException | PhoneNumberAlreadyExistsException e) {
            System.err.println("MobileContactServiceImpl logger:"  + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteContactById(Long id) throws ContactNotFoundException {

        try {
            if (!dao.userIdExists(id)) {
                throw new ContactNotFoundException("Contact with id: " + id + " not found for delete." );
            }

            System.err.println("MobileContactServiceImpl logger: contact with id: " + id + " was deleted.");
            dao.deleteById(id);
        } catch (ContactNotFoundException e) {
            System.err.println("MobileContactServiceImpl logger: " + e.getMessage());
        }
    }

    @Override
    public MobileContact getContactById(Long id) throws ContactNotFoundException {
        MobileContact mobileContact;

        try {
            mobileContact = dao.getById(id);
            if (mobileContact == null) {
                throw new ContactNotFoundException("Contact with id: " + id + " not found to show.");
            }
            return mobileContact;
        } catch (ContactNotFoundException e) {
            System.err.println("MobileContactServiceImpl logger: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<MobileContact> getAllContacts() {
        return dao.getAll();
    }

    @Override
    public MobileContact getContactByPhoneNumber(String phoneNumber) throws ContactNotFoundException {
        MobileContact mobileContact;

        try {
            mobileContact = dao.getByPhoneNumber(phoneNumber);
            if (mobileContact == null) {
                throw new ContactNotFoundException("Contact with phone number: " + phoneNumber + " not found to show.");
            }
            return mobileContact;
        } catch (ContactNotFoundException e) {
            System.err.println("MobileContactServiceImpl logger: " + e.getMessage());
            throw e;
        }

    }

    @Override
    public void deleteContactByPhoneNumber(String phoneNumber) throws ContactNotFoundException {

        try {
            if (!dao.phoneNumberExists(phoneNumber)) {
                throw new ContactNotFoundException("Contact with phone number: " + phoneNumber + " not found for delete." );
            }

            System.err.println("MobileContactServiceImpl logger: contact with phone number: " + phoneNumber + " was deleted.");
            dao.deleteByPhoneNumber(phoneNumber);
        } catch (ContactNotFoundException e) {
            System.err.println("MobileContactServiceImpl logger: " + e.getMessage());
        }
    }



}
