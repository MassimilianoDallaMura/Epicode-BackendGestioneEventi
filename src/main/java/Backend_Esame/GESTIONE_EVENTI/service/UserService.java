package Backend_Esame.GESTIONE_EVENTI.service;


import Backend_Esame.GESTIONE_EVENTI.dto.UserDto;
import Backend_Esame.GESTIONE_EVENTI.entity.User;
import Backend_Esame.GESTIONE_EVENTI.enums.Role;
import Backend_Esame.GESTIONE_EVENTI.exception.UserNotFoundException;
import Backend_Esame.GESTIONE_EVENTI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    @Autowired      //iniettare il repository
    private UserRepository userRepository;
//    @Autowired
//    private EventRepository eventRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(UserDto userDto) {       //implemento il metodo per salvare
        User user = new User();     //creo user
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.USER);    //Quando utente viene creato, in automatico è user
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // non si deve mettere la password in chiaro, prima codificiamo la pass poi la si setta.


        userRepository.save(user); // la pass salvata è stata prima codificata

        return "Utente con id" + user.getId() + " salvato correttamente";

    }

    public List<User> getAllUsers(){     //metodo per estrarre tutti gli utenti -- si può aggiungere pagificazione
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public String updateUser(int id, UserDto userDto){
       Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return "Utente con id" + user.getId() + " modificato correttamente";

        }

        else {
            throw  new UserNotFoundException("Utente con id" + id + " non trovato");

        }

    }
    public String deleteUser(int id) {
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()){
            userRepository.deleteById(id);
            return "Utente con id" + id + " eliminato correttamente";
        }
        else {
            throw  new UserNotFoundException("Utente con id" + id + " non trovato");
        }
    }

    public User getUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent()){
           return userOptional.get();
        }
        else {
            throw new UserNotFoundException("Utente con email" + email + " non trovato");
        }
    }


    public boolean existsByEmail(String email) {
        // Utilizza il repository per verificare se un utente con l'indirizzo email specificato esiste già nel database
        return userRepository.existsByEmail(email);
    }


}



