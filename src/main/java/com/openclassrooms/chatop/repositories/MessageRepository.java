package com.openclassrooms.chatop.repositories;

import com.openclassrooms.chatop.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {}