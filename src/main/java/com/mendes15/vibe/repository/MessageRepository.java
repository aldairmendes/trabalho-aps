package com.mendes15.vibe.repository;

import com.mendes15.vibe.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Aqui a mágica acontece: você ganha métodos como .save(), .findAll(), .delete() de graça!
}