package com.auction.service.impl;

import com.auction.dto.AuctionRequest;
import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Role;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuctionServiceImplTest {
    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @Test
    void createShouldAllowOwner() {
        AuctionRequest request = new AuctionRequest();
        request.setTitle("Ноутбук");
        request.setDescription("Игровой ноутбук");
        request.setStartPrice(new BigDecimal("10000.00"));
        request.setStartTime(LocalDateTime.now().minusMinutes(5));
        request.setEndTime(LocalDateTime.now().plusDays(1));

        User owner = User.builder()
                .id(1L)
                .username("owner")
                .role(Role.OWNER)
                .enabled(true)
                .build();

        when(userService.getByUsername("owner")).thenReturn(owner);
        when(auctionRepository.save(any(Auction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Auction auction = auctionService.create(request, "owner");

        assertEquals("Ноутбук", auction.getTitle());
        assertEquals(owner, auction.getOwner());
        assertEquals(AuctionStatus.ACTIVE, auction.getStatus());

        ArgumentCaptor<Auction> captor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(captor.capture());
        assertEquals(Role.OWNER, captor.getValue().getOwner().getRole());
    }

    @Test
    void createShouldRejectAdmin() {
        AuctionRequest request = new AuctionRequest();
        request.setTitle("Товар");
        request.setDescription("Описание");
        request.setStartPrice(new BigDecimal("1000.00"));
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusDays(1));

        User admin = User.builder()
                .id(2L)
                .username("admin")
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        when(userService.getByUsername("admin")).thenReturn(admin);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> auctionService.create(request, "admin"));
        assertEquals("Создавать аукционы может только владелец", exception.getMessage());
    }

    @Test
    void createShouldRejectWhenStartTimeAfterEndTime() {
        AuctionRequest request = new AuctionRequest();
        request.setTitle("Товар");
        request.setDescription("Описание");
        request.setStartPrice(new BigDecimal("1000.00"));
        request.setStartTime(LocalDateTime.now().plusDays(2));
        request.setEndTime(LocalDateTime.now().plusDays(1));

        User owner = User.builder()
                .id(1L)
                .username("owner")
                .role(Role.OWNER)
                .enabled(true)
                .build();

        when(userService.getByUsername("owner")).thenReturn(owner);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> auctionService.create(request, "owner"));
        assertEquals("Дата начала не может быть позже даты окончания", exception.getMessage());
    }
}