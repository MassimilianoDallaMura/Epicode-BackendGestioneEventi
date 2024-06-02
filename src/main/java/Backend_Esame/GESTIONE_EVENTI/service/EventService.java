package Backend_Esame.GESTIONE_EVENTI.service;

import Backend_Esame.GESTIONE_EVENTI.dto.EventDto;

import Backend_Esame.GESTIONE_EVENTI.entity.Event;

import Backend_Esame.GESTIONE_EVENTI.entity.Reservation;
import Backend_Esame.GESTIONE_EVENTI.entity.User;
import Backend_Esame.GESTIONE_EVENTI.exception.EventNotFoundException;
import Backend_Esame.GESTIONE_EVENTI.exception.MaxParteciptantsReachedException;
import Backend_Esame.GESTIONE_EVENTI.exception.UserAlreadyRegisteredException;
import Backend_Esame.GESTIONE_EVENTI.exception.UserNotFoundException;
import Backend_Esame.GESTIONE_EVENTI.repository.EventRepository;
import Backend_Esame.GESTIONE_EVENTI.repository.ReservationRepository;
import Backend_Esame.GESTIONE_EVENTI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;


    public String saveEvent (EventDto eventDto) {
        Event event = new Event();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setLocation(eventDto.getLocation());
        event.setDate(eventDto.getDate());
        event.setMaxPartecipants(eventDto.getMaxPartecipants());

        eventRepository.save(event);

        return "L'evento" + event.getTitle() + " è stato salvato correttamente";
    }

    public List<Event> getAllEvents() { return eventRepository.findAll();
    }

    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    public String updateEvent(int id, EventDto eventDto){
        Optional<Event> eventOptional = getEventById(id);

         if (eventOptional.isPresent()){
         Event event = eventOptional.get();
         event.setTitle(eventDto.getTitle());
         event.setDescription(eventDto.getDescription());
         event.setLocation(eventDto.getLocation());
         event.setMaxPartecipants(eventDto.getMaxPartecipants());
         event.setDate(eventDto.getDate());

         eventRepository.save(event);
         return "L'evento" + event.getTitle() + " è stato salvato correttamente";
         }

         else {
             throw new EventNotFoundException("Evento" + eventDto.getTitle() + "non trovato");
         }
    }


    public String deleteEvent(int id){
        Optional<Event> eventOptional = getEventById(id);

        if (eventOptional.isPresent()){
            eventRepository.deleteById(id);
            return "Evento" + id + "eliminato correttamente";
        }

        else {
            throw new EventNotFoundException("Utente con id" + id + " non trovato");
        }
    }

    @Transactional
    public String registerUserToEvent(int eventId, int userId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getSubscribedUsers().size() >= event.getMaxPartecipants()) {
                throw new MaxParteciptantsReachedException("L'evento ha già raggiunto il numero massimo di partecipanti");
            } else {
                // Controlla se l'utente è già iscritto all'evento
                boolean userAlreadyRegistered = event.getSubscribedUsers().stream()
                        .anyMatch(user -> user.getId() == userId);
                if (userAlreadyRegistered) {
                    throw new UserAlreadyRegisteredException("L'utente è già iscritto a questo evento");
                } else {
                    // Genera il codice di prenotazione
                    String bookingCode = generateBookingCode();

                    // Aggiunge l'utente alla lista degli iscritti all'evento
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("Utente con id " + userId + " non trovato"));

                    // Creazione della prenotazione
                    Reservation reservation = new Reservation();
                    reservation.setUser(user);
                    reservation.setEvent(event);
                    reservation.setBookingCode(bookingCode);

                    // Salvataggio delle entità
                    event.getSubscribedUsers().add(user);
                    eventRepository.save(event);
                    userRepository.save(user);
                    reservationRepository.save(reservation);

                    return "L'utente è stato iscritto all'evento con successo. Codice di prenotazione: " + bookingCode;
                }
            }
        } else {
            throw new EventNotFoundException("Evento con id " + eventId + " non trovato");
        }
    }

    private String generateBookingCode() {
        // Implementa la logica per generare un codice di prenotazione univoco
        // Ad esempio, puoi usare UUID.randomUUID() per generare un UUID
        return UUID.randomUUID().toString();
    }

    public List<User> getUsersByEventId(int eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId); //restituisce optional
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.getSubscribedUsers();
        } else {
            throw new EventNotFoundException("Evento con id " + eventId + " non trovato");
        }
    }

    public List<Event> getEventsByUserId(int userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            return user.getSubscribedEvents();
        }
        else {
            throw new UserNotFoundException("User con id + eventId + non trovato");
        }
    }

    public List<Event> getEventsForCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        // Recupera l'utente dal database utilizzando l'email
        // Assume che ci sia un metodo nel tuo repository per recuperare l'utente per email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Ora puoi ottenere gli eventi a cui l'utente è iscritto
        if (userOptional.isPresent()){
            User user = userOptional.get();
        return user.getSubscribedEvents();
    }
        else {
            throw new UserNotFoundException("User con id + eventId + non trovato");
        }
    }

    public void addEventToFavorites(int userId, int eventId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (userOptional.isPresent() && eventOptional.isPresent()) {
            User user = userOptional.get();
            Event event = eventOptional.get();

            if (!user.getFavoriteEvents().contains(event)) {
                user.getFavoriteEvents().add(event);
                userRepository.save(user);
            }
        } else {
            throw new RuntimeException("User or Event not found");
        }
    }



}
