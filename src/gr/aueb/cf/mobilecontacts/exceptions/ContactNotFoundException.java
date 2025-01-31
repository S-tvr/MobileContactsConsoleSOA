package gr.aueb.cf.mobilecontacts.exceptions;

public class ContactNotFoundException extends Exception{

    // απο την στιγμη που κανουμε extends πρεπει να εχουμε μια super.
    // είτε μας την δίνει η java στον δεφαθλτ ψονστρθψτορ που εννοείται
    // κανω inject το δικό μου message.
    public ContactNotFoundException(String message) {
        super(message);
    }
}
