package org.databaseservice.repository;

import org.databaseservice.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsUserEntityByUsername(String username);
}
