package patients_mvc.patients.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import patients_mvc.patients.security.service.UserDetailsServiceImpl;

import javax.sql.DataSource;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public UserDetailsService userDetailsService() {
       return new UserDetailsService() {
            @Override
           public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                return null;
            }
        };
    }

    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
    }

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1234")).authorities("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("1234")).authorities("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("1234")).authorities("USER","ADMIN").build()

        );
    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers("/webjars/**", "/h2-console/**").permitAll()
                            //.requestMatchers("/user/**").hasAuthority("USER")
                            //.requestMatchers("/admin/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated()
                    )
                    .exceptionHandling(exceptionHandling -> exceptionHandling
                            .accessDeniedPage("/notAuthorized")
                    )
                    .formLogin(formLogin -> formLogin
                            .loginPage("/login")
                            .defaultSuccessUrl("/")
                            .permitAll()
                    )
                    //.rememberMe(rememberMe -> rememberMe
                    //        .key("uniqueAndSecret") // Utilisez une clé secrète
                    //        .tokenValiditySeconds(86400) // Durée de validité du cookie (1 jour)
                    //)
                    .userDetailsService(userDetailsServiceImpl);

            return httpSecurity.build();
        }

}
