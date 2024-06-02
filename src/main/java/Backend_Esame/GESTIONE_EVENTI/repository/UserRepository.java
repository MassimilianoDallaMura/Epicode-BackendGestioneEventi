package Backend_Esame.GESTIONE_EVENTI.repository;

import Backend_Esame.GESTIONE_EVENTI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findBySubscribedEventsId(int eventId);
    }
