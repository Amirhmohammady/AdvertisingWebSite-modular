package com.mycompany.advertising.repository;

import com.mycompany.advertising.repository.entity.UserTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Amir on 6/6/2020.
 */
@Repository
public interface UserRepository extends JpaRepository<UserTo, Long> {

    Optional<UserTo> findByProfilename(String profilename);

    Optional<UserTo> findByEmail(String email);

    Optional<UserTo> findByUsername(String username);

    boolean existsByEmailAndEnabled(String email, boolean enabled);

    boolean existsByUsernameAndEnabled(String username, boolean enabled);

    boolean existsByUsername(String username);

    Long deleteByUsername(String username);

    //dont need
    //void deleteAll(List<UserTo> users);
}
