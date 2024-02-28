package org.databaseservice.repository;

import org.databaseservice.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsUserEntityByUsername(String username);
    Optional<UserEntity> getUserEntityByUsername(String username);
}
