package Backend_Esame.GESTIONE_EVENTI.controller;

import Backend_Esame.GESTIONE_EVENTI.dto.EventDto;
import Backend_Esame.GESTIONE_EVENTI.entity.Event;
import Backend_Esame.GESTIONE_EVENTI.entity.User;
import Backend_Esame.GESTIONE_EVENTI.exception.BadRequestException;
import Backend_Esame.GESTIONE_EVENTI.exception.EventNotFoundException;
import Backend_Esame.GESTIONE_EVENTI.repository.UserRepository;
import Backend_Esame.GESTIONE_EVENTI.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/events")
//    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER')")
    public String saveEvent(@RequestBody @Validated EventDto eventDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }
        return eventService.saveEvent(eventDto);
    }


    @GetMapping("/api/events")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'USER')")
    public List<Event> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/api/events/{id}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'USER')")
    public Event getEventById(@PathVariable int id){
        Optional<Event> eventOptional = eventService.getEventById(id);

        if (eventOptional.isPresent()){
            return eventOptional.get();
        }
        else {
            throw new EventNotFoundException("Evento con id" + id + " non trovato");
        }
    }


    @PutMapping("/api/events/{id}")
    @PreAuthorize("haAuthority('MANAGER')")
    public String updateEvent(@PathVariable int id, @RequestBody @Validated EventDto eventDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }
        return eventService.updateEvent(id, eventDto);
    }


    @DeleteMapping("/api/events/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String deleteEvent(@PathVariable int id){
        return eventService.deleteEvent(id);
    }


    @PostMapping("/api/events/register/{eventId}/{userId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String registerUserToEvent(@PathVariable int eventId, @PathVariable int userId) {
        return eventService.registerUserToEvent(eventId, userId);
    }


    @GetMapping("/api/events/{eventId}/users")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'USER')")
    public List<User> getUsersByEventId(@PathVariable int eventId) {
        return eventService.getUsersByEventId(eventId);
    }

    @GetMapping("/api/events/{userId}/events")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'USER')")
    public List<Event> getEventsByUserId(@PathVariable int userId) {
        return eventService.getEventsByUserId(userId);
    }

    @PostMapping("/api/events/{eventId}/favorite")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'USER')")
    public void addEventToFavorites(@PathVariable int eventId, int userId) {
        eventService.addEventToFavorites(eventId, userId);
    }

    @GetMapping("/api/user_events")
    public ResponseEntity <List<Event>> getEventsForCurrentUser(@PathVariable int user) {
        List<Event> userEvents = eventService.getEventsForCurrentUser();
        return new ResponseEntity<>(userEvents, HttpStatus.OK);
    }


}


