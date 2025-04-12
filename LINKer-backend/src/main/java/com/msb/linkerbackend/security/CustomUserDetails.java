package com.msb.linkerbackend.security;

import com.msb.linkerbackend.models.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomUserDetails extends User implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public CustomUserDetails(@NotNull User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        this.authorities.add(authority);
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

}
