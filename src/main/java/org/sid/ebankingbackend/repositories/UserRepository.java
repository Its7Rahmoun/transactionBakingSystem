package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp,Long> {

    Optional<UserApp> findByUsername(String username);
}
