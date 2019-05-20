package ru.urfu.Server.Repositories;

import org.springframework.data.repository.CrudRepository;
import ru.urfu.Server.Models.Round;

public interface RoundsRepository extends CrudRepository<Round, Integer> {
}
