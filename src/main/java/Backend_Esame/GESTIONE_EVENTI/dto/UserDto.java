package Backend_Esame.GESTIONE_EVENTI.dto;


import Backend_Esame.GESTIONE_EVENTI.entity.Event;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {          //verrà usato in fase di REGISTRAZIONE utente

    private String name;
    private  String surname;
    @Email
    @NotBlank(message = "Il campo non pù essere vuoto, mancante o composto da soli spazi")
    private String email;
    @NotBlank(message = "Il campo non pù essere vuoto, mancante o composto da soli spazi")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
            message = "La password deve contenere almeno una lettera minuscola, una maiuscola, un numero e un carattere speciale")
    private String password;

    private List<Event> subscribedEvents;
}
