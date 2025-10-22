package com.esports.tournament.repo;

import com.esports.tournament.model.Role;
import com.esports.tournament.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleAndVerified(Role role, boolean verified);

    List<User> findByRole(Role role);
}
