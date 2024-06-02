package Backend_Esame.GESTIONE_EVENTI.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public  EmailAlreadyExistsException(String message){
        super(message);
    }
}
