package ro.mariuscirstea.eventtracker.service;

import ro.mariuscirstea.eventtracker.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getUser(Long userId);

    Long findUserIdByEmail(String currentPrincipalName);
}
