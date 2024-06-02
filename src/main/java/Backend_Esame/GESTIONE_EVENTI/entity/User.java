package Backend_Esame.GESTIONE_EVENTI.entity;

import Backend_Esame.GESTIONE_EVENTI.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data@Entity
public class User implements UserDetails { //userDet ha un metodo getAuth che riporta i ruoli dell'utente

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "reservation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")

    )
    @JsonIgnore
    private List<Event> subscribedEvents;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_favorite_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
//    @JsonIgnore
    private List<Event> favoriteEvents = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); //ritorna una lista di tutt i ruoli sottoforma di oggetti grantedAutorithy interfaccia implementata da simple garantedAuthority in cui mettiamo il ruolo dell'utente. Role.name rappresenta stringa colegata a quell enum. lo riportiamo ne JwtFilter
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
