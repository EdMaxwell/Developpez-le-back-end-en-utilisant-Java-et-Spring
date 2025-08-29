package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.CreateMessageDto;
import com.openclassrooms.chatop.entities.Message;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.repositories.MessageRepository;
import com.openclassrooms.chatop.repositories.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private MessageService messageService;

    private CreateMessageDto messageDto;
    private Rental rental;
    private Message savedMessage;

    @BeforeEach
    void setUp() {
        // Set up test data
        messageDto = new CreateMessageDto();
        messageDto.setMessage("Hello, I'm interested in your rental!");
        messageDto.setUser_id(1L);
        messageDto.setRental_id(1L);

        rental = new Rental();
        rental.setId(1L);
        rental.setName("Beautiful Apartment");
        rental.setSurface(75.0);
        rental.setPrice(BigDecimal.valueOf(1200));
        rental.setDescription("A beautiful apartment in the city center");
        rental.setOwnerId(2L);

        savedMessage = new Message();
        savedMessage.setId(1L);
        savedMessage.setContent("Hello, I'm interested in your rental!");
        savedMessage.setSenderId(1L);
        savedMessage.setRecipientId(2L);
        savedMessage.setRentalId(1L);
    }

    @Test
    void sendMessage_ShouldCreateAndSaveMessage_WhenRentalExists() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        // When
        Message result = messageService.sendMessage(messageDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(savedMessage.getId(), result.getId());
        assertEquals(savedMessage.getContent(), result.getContent());
        assertEquals(savedMessage.getSenderId(), result.getSenderId());
        assertEquals(savedMessage.getRecipientId(), result.getRecipientId());
        assertEquals(savedMessage.getRentalId(), result.getRentalId());

        // Verify interactions
        verify(rentalRepository).findById(1L);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void sendMessage_ShouldSetCorrectRecipient_FromRentalOwner() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message message = invocation.getArgument(0);
            message.setId(1L);
            return message;
        });

        // When
        Message result = messageService.sendMessage(messageDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(rental.getOwnerId(), result.getRecipientId()); // Recipient should be the rental owner
        assertEquals(1L, result.getSenderId()); // Sender should be the authenticated user
        assertEquals(rental.getId(), result.getRentalId());
    }

    @Test
    void sendMessage_ShouldReturnNull_WhenRentalDoesNotExist() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Message result = messageService.sendMessage(messageDto, 1L);

        // Then
        assertNull(result);

        // Verify that save was never called
        verify(rentalRepository).findById(1L);
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void sendMessage_ShouldSetCorrectMessageContent() {
        // Given
        String customMessage = "Is this rental still available?";
        messageDto.setMessage(customMessage);
        
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message message = invocation.getArgument(0);
            message.setId(1L);
            return message;
        });

        // When
        Message result = messageService.sendMessage(messageDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(customMessage, result.getContent());
    }

    @Test
    void sendMessage_ShouldHandleDifferentSenderIds() {
        // Given
        Long senderId = 99L;
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message message = invocation.getArgument(0);
            message.setId(1L);
            return message;
        });

        // When
        Message result = messageService.sendMessage(messageDto, senderId);

        // Then
        assertNotNull(result);
        assertEquals(senderId, result.getSenderId());
        assertEquals(rental.getOwnerId(), result.getRecipientId());
    }

    @Test
    void sendMessage_ShouldPreserveAllMessageFields() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
            Message message = invocation.getArgument(0);
            message.setId(1L);
            return message;
        });

        // When
        Message result = messageService.sendMessage(messageDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(messageDto.getMessage(), result.getContent());
        assertEquals(1L, result.getSenderId());
        assertEquals(rental.getOwnerId(), result.getRecipientId());
        assertEquals(rental.getId(), result.getRentalId());

        // Verify the message was saved with correct values
        verify(messageRepository).save(argThat(message -> 
            message.getContent().equals(messageDto.getMessage()) &&
            message.getSenderId().equals(1L) &&
            message.getRecipientId().equals(rental.getOwnerId()) &&
            message.getRentalId().equals(rental.getId())
        ));
    }
}