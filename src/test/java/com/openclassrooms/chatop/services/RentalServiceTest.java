package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.CreateRentalDto;
import com.openclassrooms.chatop.dtos.RentalListItemDto;
import com.openclassrooms.chatop.dtos.UpdateRentalDto;
import com.openclassrooms.chatop.entities.Rental;
import com.openclassrooms.chatop.repositories.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    private CreateRentalDto createRentalDto;
    private UpdateRentalDto updateRentalDto;
    private Rental rental;
    private MockMultipartFile validImageFile;
    private MockMultipartFile invalidFile;
    private MockMultipartFile largeFile;

    @BeforeEach
    void setUp() {
        // Create test image file
        validImageFile = new MockMultipartFile(
                "picture",
                "test.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        // Create invalid file (not an image)
        invalidFile = new MockMultipartFile(
                "document",
                "test.txt",
                "text/plain",
                "this is not an image".getBytes()
        );

        // Create large file (over 5MB)
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB
        largeFile = new MockMultipartFile(
                "picture",
                "large.jpg",
                "image/jpeg",
                largeContent
        );

        // Set up DTOs
        createRentalDto = new CreateRentalDto();
        createRentalDto.setName("Beautiful Apartment");
        createRentalDto.setSurface(75.0);
        createRentalDto.setPrice(BigDecimal.valueOf(1200));
        createRentalDto.setDescription("A beautiful apartment in the city center");
        createRentalDto.setPicture(validImageFile);

        updateRentalDto = new UpdateRentalDto();
        updateRentalDto.setName("Updated Apartment");
        updateRentalDto.setSurface(80.0);
        updateRentalDto.setPrice(BigDecimal.valueOf(1300));
        updateRentalDto.setDescription("Updated description");

        // Set up rental entity
        rental = new Rental();
        rental.setId(1L);
        rental.setName("Beautiful Apartment");
        rental.setSurface(75.0);
        rental.setPrice(BigDecimal.valueOf(1200));
        rental.setDescription("A beautiful apartment in the city center");
        rental.setOwnerId(1L);
        rental.setCreatedAt(Instant.now());
        rental.setUpdatedAt(Instant.now());
    }

    @Test
    void createRental_ShouldCreateRental_WhenValidData() throws IOException {
        // Given
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental savedRental = invocation.getArgument(0);
            savedRental.setId(1L);
            return savedRental;
        });

        // When
        Rental result = rentalService.createRental(createRentalDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(createRentalDto.getName(), result.getName());
        assertEquals(createRentalDto.getSurface(), result.getSurface());
        assertEquals(createRentalDto.getPrice(), result.getPrice());
        assertEquals(createRentalDto.getDescription(), result.getDescription());
        assertEquals(1L, result.getOwnerId());

        // Check picture fields
        assertNotNull(result.getPicture());
        assertEquals("image/jpeg", result.getPictureContentType());
        assertEquals(validImageFile.getSize(), result.getPictureSize());
        assertNotNull(result.getPictureFilename());
        assertTrue(result.getPictureFilename().endsWith(".jpg"));

        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void createRental_ShouldThrowException_WhenFileIsTooLarge() {
        // Given
        createRentalDto.setPicture(largeFile);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.createRental(createRentalDto, 1L)
        );
        assertEquals("File too large (max 5MB)", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void createRental_ShouldThrowException_WhenFileIsNotAnImage() {
        // Given
        createRentalDto.setPicture(invalidFile);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rentalService.createRental(createRentalDto, 1L)
        );
        assertEquals("File must be an image", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void createRental_ShouldCreateRental_WhenPictureIsEmpty() throws IOException {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile("picture", "", "image/jpeg", new byte[0]);
        createRentalDto.setPicture(emptyFile);

        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental savedRental = invocation.getArgument(0);
            savedRental.setId(1L);
            return savedRental;
        });

        // When
        Rental result = rentalService.createRental(createRentalDto, 1L);

        // Then
        assertNotNull(result);
        assertNull(result.getPicture()); // No picture should be set for empty file
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void createRental_ShouldCreateRental_WhenPictureIsNull() throws IOException {
        // Given
        createRentalDto.setPicture(null);

        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental savedRental = invocation.getArgument(0);
            savedRental.setId(1L);
            return savedRental;
        });

        // When
        Rental result = rentalService.createRental(createRentalDto, 1L);

        // Then
        assertNotNull(result);
        assertNull(result.getPicture()); // No picture should be set
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void findById_ShouldReturnRental_WhenRentalExists() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        // When
        Rental result = rentalService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(rental.getId(), result.getId());
        assertEquals(rental.getName(), result.getName());
        verify(rentalRepository).findById(1L);
    }

    @Test
    void findById_ShouldReturnNull_WhenRentalDoesNotExist() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Rental result = rentalService.findById(1L);

        // Then
        assertNull(result);
        verify(rentalRepository).findById(1L);
    }

    @Test
    void findAllRentals_ShouldReturnMappedRentalList() {
        // Given
        Rental rental2 = new Rental();
        rental2.setId(2L);
        rental2.setName("Second Rental");
        rental2.setSurface(60.0);
        rental2.setPrice(BigDecimal.valueOf(1000));
        rental2.setDescription("Second rental description");
        rental2.setOwnerId(2L);
        rental2.setCreatedAt(Instant.now());
        rental2.setUpdatedAt(Instant.now());

        List<Rental> rentals = Arrays.asList(rental, rental2);
        when(rentalRepository.findAll()).thenReturn(rentals);

        // When
        List<RentalListItemDto> result = rentalService.findAllRentals();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        RentalListItemDto dto1 = result.get(0);
        assertEquals(rental.getId(), dto1.getId());
        assertEquals(rental.getName(), dto1.getName());
        assertEquals(rental.getSurface(), dto1.getSurface());
        assertEquals(rental.getPrice(), dto1.getPrice());
        assertEquals("/api/rentals/" + rental.getId() + "/picture", dto1.getPicture());
        assertEquals(rental.getOwnerId(), dto1.getOwner_id());

        verify(rentalRepository).findAll();
    }

    @Test
    void updateRental_ShouldUpdateRental_WhenUserIsOwner() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Rental result = rentalService.updateRental(1L, updateRentalDto, 1L);

        // Then
        assertNotNull(result);
        assertEquals(updateRentalDto.getName(), result.getName());
        assertEquals(updateRentalDto.getSurface(), result.getSurface());
        assertEquals(updateRentalDto.getPrice(), result.getPrice());
        assertEquals(updateRentalDto.getDescription(), result.getDescription());

        verify(rentalRepository).findById(1L);
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void updateRental_ShouldReturnNull_WhenUserIsNotOwner() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        // When - try to update with different user ID
        Rental result = rentalService.updateRental(1L, updateRentalDto, 999L);

        // Then
        assertNull(result);
        verify(rentalRepository).findById(1L);
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void updateRental_ShouldReturnNull_WhenRentalDoesNotExist() {
        // Given
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Rental result = rentalService.updateRental(1L, updateRentalDto, 1L);

        // Then
        assertNull(result);
        verify(rentalRepository).findById(1L);
        verify(rentalRepository, never()).save(any(Rental.class));
    }
}