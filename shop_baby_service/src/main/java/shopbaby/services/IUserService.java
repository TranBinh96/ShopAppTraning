package shopbaby.services;


import shopbaby.dtos.UserDTO;
import shopbaby.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password, Long roleId) throws Exception;
}
