package com.example.vehicleservice.config;

import com.example.vehicleservice.admin.repository.UserRepository;
import com.example.vehicleservice.admin.repository.UserRoleRepository;
import com.example.vehicleservice.config.security.UserDetail;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public UserDetail loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findUserLoginDetailsRecordFromUserByUseUsername(username)
                .map(user -> {
                    // Fetch user roles by username
                    List<String> userRoles = userRoleRepository.findUsrNameByUsrUseUsername(username);

                    // Convert roles to GrantedAuthority using Mapping
                    List<GrantedAuthority> authorities = userRoles.stream()
                            .collect(Collectors.mapping(SimpleGrantedAuthority::new, Collectors.toList()));

                    // Return the custom user details object
                    return new UserDetail(
                            user.useUsername(),
                            user.usePassword(),
                            authorities
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found for username " + username));
    }
}
