package ru.urfu.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import ru.urfu.Server.Models.User;

import java.util.Set;

public interface UsersRepository extends CrudRepository<User, Integer> {
    User findByUserNameAndAndHashedPassword(String userName, String hashedPassword);
    User findByUserName(String userName);
}
