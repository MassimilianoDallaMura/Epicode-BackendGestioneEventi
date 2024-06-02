package Backend_Esame.GESTIONE_EVENTI.repository;

import Backend_Esame.GESTIONE_EVENTI.entity.Event;
import Backend_Esame.GESTIONE_EVENTI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {

}
