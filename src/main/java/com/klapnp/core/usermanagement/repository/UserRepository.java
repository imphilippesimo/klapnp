package com.klapnp.core.usermanagement.repository;

import com.klapnp.core.usermanagement.model.KlapUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<KlapUser, Long> {

    Optional<KlapUser> findOneByActivationKey(String activationKey);

    List<KlapUser> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<KlapUser> findOneByResetKey(String resetKey);

    Optional<KlapUser> findOneByEmailIgnoreCase(String email);

    Optional<KlapUser> findOneByLogin(String login);

    Page<KlapUser> findAllByLoginNot(Pageable pageable, String login);

}
