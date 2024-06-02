package Backend_Esame.GESTIONE_EVENTI.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity(debug = true) //debTr crea log più verboso con più info
@EnableMethodSecurity //permette di attivare sicurezza sui metodi del controller con il PreAuth
public class Config {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(http -> http.disable()); // eliminare autenticazoine attraverso form di login
        httpSecurity.csrf(http -> http.disable());    //disabilita cfr(protezione) lo disabilitiamo perxhé la gestione è troppo complicata
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //qui si possono mettere tutte le istruzioni che si vogliono con i filtri:

        //permette l'accesso a tutti dei servizi con endpoint /api/users e metodi get ( dopo l'autenticazione)
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/api/**").permitAll()); //si definisce i pattern autorizzati (o quale pattern può quale metodo)

        httpSecurity.authorizeHttpRequests(http->http.requestMatchers(HttpMethod.POST,"/auth/**").permitAll());

        //nega l'accesso a qualsiasi servizio che non sia get e path /api/users
        httpSecurity.authorizeHttpRequests(http->http.anyRequest().authenticated());
        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //viene creato un oggetto bcrpasswordencoder per poi poterlo chiamare nel punto opportuno
    }

    @Bean//permette di abilitare l'accesso al servizio anche da parte di server diversi da quello su cui risiede
    //il servizio. In questo caso abilitata tutti i server ad accedere a tutti i servizi
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
