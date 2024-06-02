package Backend_Esame.GESTIONE_EVENTI.exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(String message){
        super (message);
    }
}
