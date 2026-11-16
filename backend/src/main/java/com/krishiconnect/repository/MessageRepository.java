package com.krishiconnect.repository;

import com.krishiconnect.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender.id = :u1 AND m.receiver.id = :u2) OR (m.sender.id = :u2 AND m.receiver.id = :u1)) AND " +
           "(:cropId IS NULL OR m.crop.id = :cropId) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("u1") Long u1, @Param("u2") Long u2, @Param("cropId") Long cropId);
}
