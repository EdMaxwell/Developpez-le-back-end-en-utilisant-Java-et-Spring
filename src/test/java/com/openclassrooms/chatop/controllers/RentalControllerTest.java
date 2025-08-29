package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.RentalListItemDto;
import com.openclassrooms.chatop.dtos.RentalListResponse;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.dtos.UpdateRentalDto;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.entities.User;
import com.openclassrooms.chatop.services.RentalService;
import com.openclassrooms.chatop.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RentalController rentalController;

    private Rental rental;
    private User user;
    private UpdateRentalDto updateRentalDto;
    private RentalListItemDto rentalListItemDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up test user
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // Set up test rental
        rental = new Rental();
        rental.setId(1L);
        rental.setName("Beautiful Apartment");
        rental.setSurface(75.0);
        rental.setPrice(BigDecimal.valueOf(1200));
        rental.setDescription("A beautiful apartment in the city center");
        rental.setOwnerId(1L);
        rental.setCreatedAt(Instant.now());
        rental.setUpdatedAt(Instant.now());
        rental.setPicture("fake image content".getBytes());
        rental.setPictureContentType("image/jpeg");
        rental.setPictureFilename("test.jpg");
        rental.setPictureSize(100L);

        // Set up update DTO
        updateRentalDto = new UpdateRentalDto();
        updateRentalDto.setName("Updated Apartment");
        updateRentalDto.setSurface(80.0);
        updateRentalDto.setPrice(BigDecimal.valueOf(1300));
        updateRentalDto.setDescription("Updated description");

        // Set up rental list item DTO
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneId.systemDefault());
        rentalListItemDto = new RentalListItemDto(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                "/api/rentals/" + rental.getId() + "/picture",
                rental.getDescription(),
                rental.getOwnerId(),
                formatter.format(rental.getCreatedAt()),
                formatter.format(rental.getUpdatedAt())
        );
    }

    @Test
    void getAllRentals_ShouldReturnRentalsList() {
        // Given
        List<RentalListItemDto> rentals = Arrays.asList(rentalListItemDto);
        when(rentalService.findAllRentals()).thenReturn(rentals);

        // When
        ResponseEntity<RentalListResponse> response = rentalController.getAllRentals();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getRentals());
        assertEquals(1, response.getBody().getRentals().size());
        assertEquals("Beautiful Apartment", response.getBody().getRentals().get(0).getName());
    }

    @Test
    void getRental_ShouldReturnRental_WhenRentalExists() {
        // Given
        when(rentalService.findById(1L)).thenReturn(rental);

        // When
        ResponseEntity<RentalResponse> response = rentalController.getRental(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Beautiful Apartment", response.getBody().getName());
        assertEquals(75.0, response.getBody().getSurface());
        assertEquals(BigDecimal.valueOf(1200), response.getBody().getPrice());
        assertEquals("/api/rentals/1/picture", response.getBody().getPicture()[0]);
    }

    @Test
    void getRental_ShouldReturnNotFound_WhenRentalDoesNotExist() {
        // Given
        when(rentalService.findById(1L)).thenReturn(null);

        // When
        ResponseEntity<RentalResponse> response = rentalController.getRental(1L);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getRentalPicture_ShouldReturnImage_WhenRentalExists() {
        // Given
        when(rentalService.findById(1L)).thenReturn(rental);

        // When
        ResponseEntity<byte[]> response = rentalController.getRentalPicture(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals("fake image content".getBytes(), response.getBody());
        assertEquals("image/jpeg", response.getHeaders().getContentType().toString());
    }

    @Test
    void getRentalPicture_ShouldReturnNotFound_WhenRentalDoesNotExist() {
        // Given
        when(rentalService.findById(1L)).thenReturn(null);

        // When
        ResponseEntity<byte[]> response = rentalController.getRentalPicture(1L);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getRentalPicture_ShouldReturnNotFound_WhenRentalHasNoPicture() {
        // Given
        rental.setPicture(null);
        when(rentalService.findById(1L)).thenReturn(rental);

        // When
        ResponseEntity<byte[]> response = rentalController.getRentalPicture(1L);

        // Then
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void updateRental_ShouldReturnSuccess_WhenValidInput() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(rentalService.updateRental(eq(1L), any(UpdateRentalDto.class), eq(1L))).thenReturn(rental);

        // When
        ResponseEntity<?> response = rentalController.updateRental(1L, updateRentalDto);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals("Rental updated !", responseBody.get("message"));

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateRental_ShouldReturnUnauthorized_WhenRentalNotFoundOrNotOwner() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(rentalService.updateRental(eq(1L), any(UpdateRentalDto.class), eq(1L))).thenReturn(null);

        // When
        ResponseEntity<?> response = rentalController.updateRental(1L, updateRentalDto);

        // Then
        assertEquals(401, response.getStatusCodeValue()); // Controller returns 401 when service returns null
        assertNull(response.getBody());

        // Cleanup
        SecurityContextHolder.clearContext();
    }

    @Test
    void updateRental_ShouldReturnUnauthorized_WhenUserNotFound() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("unknown@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByEmail("unknown@example.com")).thenReturn(null);

        // When
        ResponseEntity<?> response = rentalController.updateRental(1L, updateRentalDto);

        // Then
        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());

        // Cleanup
        SecurityContextHolder.clearContext();
    }
}