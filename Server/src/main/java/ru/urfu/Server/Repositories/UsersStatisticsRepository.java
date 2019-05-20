package ru.urfu.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import ru.urfu.Server.Models.UserStatistics;

public interface UsersStatisticsRepository extends CrudRepository<UserStatistics, Integer> {
}
