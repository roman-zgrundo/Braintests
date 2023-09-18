package by.zgrundo.braintests.config;

import by.zgrundo.braintests.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();


        return http
                .authorizeRequests()
                .antMatchers("/editrt/{id}", "/runningtext/add").hasRole("ADMIN")
                .antMatchers("/editflashingwords/{id}", "/flashingwords/add").hasRole("ADMIN")
                .antMatchers("/editdistributewords/{id}", "/distributewords/add").hasRole("ADMIN")
                .antMatchers("/accounts", "/adminedituser/{id}", "/teachers", "/students/teacher/{id}").hasRole("ADMIN")
                .antMatchers("/registration", "/students", "/homework/{id}", "/accounts/profile/{id}", "/accounts/profile/{id}/{count_sort}", "/accounts/profile/{id}/balance").hasAnyRole("ADMIN", "TEACHER")
                .antMatchers("/runningtext", "/runningtext/{id}").hasAnyRole("ADMIN", "USER", "TEACHER")
                .antMatchers("/flashingwords", "/flashingwords/{id}").hasAnyRole("ADMIN", "USER", "TEACHER")
                .antMatchers("/schultetable").hasAnyRole("ADMIN", "USER", "TEACHER")
                .antMatchers("/stroop").hasAnyRole("ADMIN", "USER", "TEACHER")
                .antMatchers("/distributewords", "/distributewords/{id}").hasAnyRole("ADMIN", "USER", "TEACHER")
                .antMatchers("/profile", "/profile/**").hasAnyRole("ADMIN", "USER", "TEACHER")
                .antMatchers("/login", "/", "/css/**", "/image/**").permitAll()
                .and()
                .csrf().disable()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and()
                .authenticationManager(authenticationManager)
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}