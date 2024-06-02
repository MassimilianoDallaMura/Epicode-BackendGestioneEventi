package Backend_Esame.GESTIONE_EVENTI.exception;

public class UserAlreadyRegisteredException extends RuntimeException{
    public UserAlreadyRegisteredException(String message){
        super(message);
    }
}
