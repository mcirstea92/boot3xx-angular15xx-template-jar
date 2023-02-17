package ro.mariuscirstea.template.service;

import ro.mariuscirstea.template.entity.USR_User;

import java.util.List;

public interface UserService {

    List<USR_User> getAll();

    USR_User getUser(Long userId);

    Long findUserIdByEmail(String currentPrincipalName);
}
