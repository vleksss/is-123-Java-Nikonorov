package com.auction.service.impl;

import com.auction.dto.AuctionRequest;
import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Role;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.service.AuctionService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final UserService userService;

    @Override
    public Auction create(AuctionRequest request, String username) {
        User owner = userService.getByUsername(username);
        if (owner.getRole() != Role.OWNER) {
            throw new IllegalStateException("Создавать аукционы может только владелец");
        }
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new IllegalArgumentException("Дата начала не может быть позже даты окончания");
        }
        AuctionStatus status = request.getStartTime().isAfter(LocalDateTime.now()) ? AuctionStatus.DRAFT : AuctionStatus.ACTIVE;
        Auction auction = Auction.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startPrice(request.getStartPrice())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(status)
                .owner(owner)
                .build();
        return auctionRepository.save(auction);
    }

    @Override
    public Auction getById(Long id) {
        return auctionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Аукцион не найден"));
    }

    @Override
    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    @Override
    public List<Auction> getActive() {
        return auctionRepository.findByStatusOrderByEndTimeAsc(AuctionStatus.ACTIVE);
    }

    @Override
    public List<Auction> getByOwner(String username) {
        return auctionRepository.findByOwnerOrderByEndTimeDesc(userService.getByUsername(username));
    }

    @Override
    public Auction close(Long id) {
        Auction auction = getById(id);
        auction.setStatus(AuctionStatus.CLOSED);
        return auctionRepository.save(auction);
    }
}