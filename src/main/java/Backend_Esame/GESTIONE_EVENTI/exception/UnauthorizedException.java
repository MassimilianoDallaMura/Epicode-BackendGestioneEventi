package Backend_Esame.GESTIONE_EVENTI.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message){
        super(message);
    }
}
