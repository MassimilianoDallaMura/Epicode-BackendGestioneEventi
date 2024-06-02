package Backend_Esame.GESTIONE_EVENTI.exception;

public class MaxParteciptantsReachedException extends RuntimeException{

    public MaxParteciptantsReachedException(String message){
        super(message);
    }
}
