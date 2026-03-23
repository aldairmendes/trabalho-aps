package com.mendes15.vibe.repository;

import com.mendes15.vibe.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop10ByRoomIdOrderByTimestampDesc(String roomId);
}