package gr.aueb.cf.mobilecontacts.validation;

import gr.aueb.cf.mobilecontacts.dto.MobileContactInsertDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactUpdateDTO;

public class ValidationUtil {

    /**
     * No instance of this Utility class should be available.
     */
    private ValidationUtil(){}

    public static String validateDTO(MobileContactInsertDTO insertDTO) {
        String errorResponse = "";
        //ερχονται trimαρισμένα
        if (insertDTO.getPhoneNumber().length() <= 5) {
            errorResponse += "Ο τηλεφωνικός αριθμός πρέπει να έχει περισσότερα απο 5 σύμβολα.\n";
        }
        if (insertDTO.getFirstname().length() <= 2)
            errorResponse += "Tο όνομα πρέπει να έχει 2 ή περισσότερους χαρακτήρες.\n";
        if (insertDTO.getLastname().length() <= 2)
            errorResponse += "Tο επίθετο πρέπει να έχει 2 ή περισσότερους χαρακτήρες.\n";

        return errorResponse;
    }

    public static String validateDTO(MobileContactUpdateDTO updateDTO) {
        String errorResponse = "";
        //ερχονται trimαρισμένα
        if (updateDTO.getPhoneNumber().length() <= 5) {
            errorResponse += "Ο τηλεφωνικός αριθμός πρέπει να έχει περισσότερα απο 5 σύμβολα.\n";
        }
        if (updateDTO.getFirstname().length() <= 2)
            errorResponse += "Tο όνομα πρέπει να έχει 2 ή περισσότερους χαρακτήρες.\n";
        if (updateDTO.getLastname().length() <= 2)
            errorResponse += "Tο επίθετο πρέπει να έχει 2 ή περισσότερους χαρακτήρες.\n";

        return errorResponse;
    }

}
