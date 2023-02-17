package ro.mariuscirstea.template.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.mariuscirstea.template.entity.USR_User;
import ro.mariuscirstea.template.exception.TypeNotFoundException;
import ro.mariuscirstea.template.repository.UserRepository;
import ro.mariuscirstea.template.security.UserPrincipal;
import ro.mariuscirstea.template.service.UserService;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<USR_User> getAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public USR_User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new TypeNotFoundException("User", userId));
    }

    public UserDetails loadUserByEmail(String email) {
        USR_User user = userRepository
                .findUSR_UserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '" + email + "' not found!"));
        return UserPrincipal.build(user);
    }

    @Override
    public Long findUserIdByEmail(String email) {
        return userRepository
                .findUSR_UserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '" + email + "' not found!"))
                .getId();
    }

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        USR_User usr = null;
        if (isEmail(user)) {
            usr = userRepository
                    .findUSR_UserByEmail(user)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found for email : " + user));
        } else {
            usr = userRepository
                    .findUSR_UserByUsername(user)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found for username : " + user));
        }
        return UserPrincipal.build(usr);
    }

    private boolean isEmail(String user) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern
                .compile(regexPattern)
                .matcher(user)
                .matches();
    }

}
