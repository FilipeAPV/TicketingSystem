package com.mycompany.ticketingsystem.security;

import com.mycompany.ticketingsystem.model.User;
import com.mycompany.ticketingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationProviderPswEmail implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationProviderPswEmail(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    Verify if the authentication object if of the type UsernamePasswordAuthenticationToken.class.
    If yes, we execute the logic inside the next method.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /*
    The authentication object as input parameter will have the username and password entered in the login page.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String psw = authentication.getCredentials().toString();

        User user = userRepository.findByEmail(email);
        if (user != null && user.getId() > 0 && passwordEncoder.matches(psw, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    user.getEmail(), null, getGrantedAuthorities(user)
                    /*
                    Changed from user.getFirstName to .getEmail() because of this line in the /dashboardController

                     User userLoggedIn  = userRepository.findByEmail(authentication.getName());
                     */
            );
        } else {
            throw new BadCredentialsException("Invalid Credentials!");
        }

    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
        return grantedAuthorities;
    }

}
