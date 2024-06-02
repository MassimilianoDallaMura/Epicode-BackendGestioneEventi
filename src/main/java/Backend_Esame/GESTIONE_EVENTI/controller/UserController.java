package Backend_Esame.GESTIONE_EVENTI.controller;
import Backend_Esame.GESTIONE_EVENTI.dto.UserDto;
import Backend_Esame.GESTIONE_EVENTI.entity.User;
import Backend_Esame.GESTIONE_EVENTI.exception.BadRequestException;
import Backend_Esame.GESTIONE_EVENTI.exception.UserNotFoundException;
import Backend_Esame.GESTIONE_EVENTI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/api/users")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'USER')")
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }


    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable int id){
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isPresent()){
            return userOptional.get();
        }
        else {
            throw new UserNotFoundException("Utente con id" + id + " non trovato");
        }
    }


    @PutMapping("/api/users/{id}")
    public String updateUser(@PathVariable int id, @RequestBody @Validated UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }
        return userService.updateUser(id, userDto);
    }


    @DeleteMapping("/api/users/{id}")
    @PreAuthorize("hasAuthority('MANAGER', 'USER')")
    public String deleteUser(@PathVariable int id){
        return userService.deleteUser(id);
    }




}
