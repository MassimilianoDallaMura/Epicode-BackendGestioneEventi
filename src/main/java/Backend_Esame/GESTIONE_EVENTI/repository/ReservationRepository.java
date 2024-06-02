package Backend_Esame.GESTIONE_EVENTI.repository;

import Backend_Esame.GESTIONE_EVENTI.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
