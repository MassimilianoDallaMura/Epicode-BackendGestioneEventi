package Backend_Esame.GESTIONE_EVENTI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EventDto {

    @NotBlank(message = "Il titolo non può essere vuoto")
    private String title;
    private String description;
    private String date;
    @NotBlank(message = "La posizione non può essere vuota")
    private String location;
    private int maxPartecipants;


    private List<UserDto> subscribedUsers;

}
