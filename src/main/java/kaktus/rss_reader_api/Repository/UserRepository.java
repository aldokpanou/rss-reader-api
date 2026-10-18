package kaktus.rss_reader_api.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kaktus.rss_reader_api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
}
