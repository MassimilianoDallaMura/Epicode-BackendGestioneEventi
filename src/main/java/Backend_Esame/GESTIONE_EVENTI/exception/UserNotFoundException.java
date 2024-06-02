package Backend_Esame.GESTIONE_EVENTI.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException (String message) {
        super(message);
    }
}
