package ru.green.nca.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.green.nca.entity.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("Logged user info: " + user);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        switch (user.getRole()) {
            case ADMIN -> authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case JOURNALIST -> authorities.add(new SimpleGrantedAuthority("ROLE_JOURNALIST"));
            default -> authorities.add(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"));
        }
        log.info("Granted authority - " + authorities + " for " + user.getUsername());
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return this.user;
    }
}
