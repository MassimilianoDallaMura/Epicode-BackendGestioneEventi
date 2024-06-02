package Backend_Esame.GESTIONE_EVENTI.controller;


import Backend_Esame.GESTIONE_EVENTI.dto.UserDto;
import Backend_Esame.GESTIONE_EVENTI.dto.UserLoginDto;
import Backend_Esame.GESTIONE_EVENTI.exception.BadRequestException;
import Backend_Esame.GESTIONE_EVENTI.exception.EmailAlreadyExistsException;
import Backend_Esame.GESTIONE_EVENTI.service.AuthService;
import Backend_Esame.GESTIONE_EVENTI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService; //verifica se è autenticato e posso dare token
    @Autowired
    private UserService userService; //registra l'utente

    @PostMapping("/auth/register")
    private String register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }

        // Verifica se l'email è già presente nel database
        if (userService.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("L'indirizzo email è già registrato.");
        }

        return userService.saveUser(userDto);
    }
    @PostMapping("/auth/login")
    public String login (@RequestBody @Validated UserLoginDto userLoginDto, BindingResult bindingResult){  //se il metodo funziona torna un token

        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error-> error.getDefaultMessage()).
                    reduce("", (s, s2) -> s+s2));
        }

        return authService.authenticateUserAndCreateToken(userLoginDto);
    }
}




