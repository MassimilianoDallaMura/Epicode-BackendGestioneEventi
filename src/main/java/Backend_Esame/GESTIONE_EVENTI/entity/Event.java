package Backend_Esame.GESTIONE_EVENTI.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;

import java.util.List;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue
    private int id;

    private String title;
    private String description;
    private String date;
    private int maxPartecipants;
    private String location;


    @ManyToMany(mappedBy = "subscribedEvents")
    private List<User> subscribedUsers;

    @ManyToMany(mappedBy = "favoriteEvents", fetch = FetchType.LAZY)
    private List<User> favoritedByUsers;
}
