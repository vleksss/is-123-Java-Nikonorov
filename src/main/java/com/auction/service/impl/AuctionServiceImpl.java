package com.auction.service.impl;

import com.auction.dto.AuctionRequest;
import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Bid;
import com.auction.model.Role;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.BidRepository;
import com.auction.service.AuctionService;
import com.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
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
        refreshStatuses();
        return auctionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Аукцион не найден"));
    }

    @Override
    public List<Auction> getAll() {
        refreshStatuses();
        return auctionRepository.findAll().stream()
                .sorted(Comparator.comparing(Auction::getEndTime).reversed())
                .toList();
    }

    @Override
    public List<Auction> getActive() {
        refreshStatuses();
        return auctionRepository.findByStatusOrderByEndTimeAsc(AuctionStatus.ACTIVE);
    }

    @Override
    public List<Auction> getByOwner(String username) {
        refreshStatuses();
        return auctionRepository.findByOwnerOrderByEndTimeDesc(userService.getByUsername(username));
    }

    @Override
    public List<Auction> getParticipating(String username) {
        refreshStatuses();
        User user = userService.getByUsername(username);
        return bidRepository.findByUserOrderByBidTimeDesc(user).stream()
                .map(Bid::getAuction)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(Auction::getEndTime).reversed())
                .toList();
    }

    @Override
    public List<Auction> getWinning(String username) {
        User user = userService.getByUsername(username);
        return getParticipating(username).stream()
                .filter(auction -> auction.getStatus() == AuctionStatus.CLOSED)
                .filter(auction -> bidRepository.findTopByAuctionOrderByAmountDesc(auction)
                        .map(bid -> bid.getUser().getId().equals(user.getId()))
                        .orElse(false))
                .toList();
    }

    @Transactional
    @Override
    public Auction closeByAdmin(Long id) {
        Auction auction = getById(id);
        auction.setStatus(AuctionStatus.CLOSED);
        return auctionRepository.save(auction);
    }

    @Transactional
    @Override
    public Auction closeByOwner(Long id, String username) {
        Auction auction = getById(id);
        if (!auction.getOwner().getUsername().equals(username)) {
            throw new IllegalStateException("Можно закрыть только свой аукцион");
        }
        auction.setStatus(AuctionStatus.CLOSED);
        return auctionRepository.save(auction);
    }

    @Transactional
    protected void refreshStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> changed = auctionRepository.findAll().stream()
                .filter(auction -> updateStatus(auction, now))
                .toList();
        if (!changed.isEmpty()) {
            auctionRepository.saveAll(changed);
        }
    }

    private boolean updateStatus(Auction auction, LocalDateTime now) {
        AuctionStatus oldStatus = auction.getStatus();
        if (auction.getEndTime() != null && !auction.getEndTime().isAfter(now)) {
            auction.setStatus(AuctionStatus.CLOSED);
        } else if (auction.getStartTime() != null && !auction.getStartTime().isAfter(now) && auction.getStatus() == AuctionStatus.DRAFT) {
            auction.setStatus(AuctionStatus.ACTIVE);
        }
        return oldStatus != auction.getStatus();
    }
}

