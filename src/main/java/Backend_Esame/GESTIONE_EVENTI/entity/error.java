package Backend_Esame.GESTIONE_EVENTI.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.time.LocalDateTime;

@Data
public class error {

    private String message;

    private LocalDateTime dataErrore;

    private HttpStatus statoErrore;

}
