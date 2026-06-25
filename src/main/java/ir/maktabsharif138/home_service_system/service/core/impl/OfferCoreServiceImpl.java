package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OfferStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.OfferRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.OfferCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OfferCoreServiceImpl implements OfferCoreService {

    private final OfferRepository offerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerOrderCoreService customerOrderCoreService;

    @Override
    @Transactional
    public Offer createOffer(Offer offer) {

        CustomerOrder order = offer.getCustomerOrder();
        Expert expert = offer.getExpert();

        if (offer.getProposedStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start date must be in future");
        }

        validateOfferCreation(expert, order);

        boolean firstOffer = !offerRepository.existsByCustomerOrderId(order.getId());
        offer.setOfferDate(LocalDateTime.now());
        offer.setOfferStatus(OfferStatus.PENDING);

        Offer saved = offerRepository.save(offer);

        if (firstOffer) {
            order.setOrderStatus(OrderStatus.WAITING_FOR_SELECTION);
        }
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Offer> findByExpertId(Long expertId, Pageable pageable) {
        return offerRepository.findByExpertId(expertId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Offer> findByOrderIdSortedByPrice(Long orderId, Pageable pageable) {
        return offerRepository.findByCustomerOrderIdOrderByProposedPriceAsc(orderId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Offer> findByOrderIdSortedByExpertRating(Long orderId, Pageable pageable) {
        return offerRepository.findByOrderIdOrderByExpertRating(orderId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Offer findById(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Offer not found"));
    }

    @Override
    @Transactional
    public Offer acceptOffer(Long orderId, Long offerId) {
        CustomerOrder order = customerOrderCoreService.findById(orderId);
        Offer offer = findById(offerId);
        checkOfferForAcceptance(offer);
        checkOfferBelongsToOrder(offer, orderId);
        checkOrderForSelection(order);
        offer.setOfferStatus(OfferStatus.ACCEPTED);
        order.setAcceptedOffer(offer);
        order.setFinalPrice(offer.getProposedPrice());
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT);
        offerRepository.save(offer);
        customerOrderRepository.save(order);


        rejectOtherOffers(order, offerId);
        return offer;
    }
    private void checkOrderForSelection(CustomerOrder order) {

        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_SELECTION) {
            throw new BadRequestException(
                    "Order is not waiting for selection"
            );
        }
    }
    private void checkOfferForAcceptance(Offer offer) {
        if (offer.getOfferStatus() != OfferStatus.PENDING) {
            throw new BadRequestException("Offer already processed");
        }
    }
    private void checkOfferBelongsToOrder(Offer offer, Long orderId) {
        if (!offer.getCustomerOrder().getId().equals(orderId)) {
            throw new BadRequestException(
                    "Offer does not belong to this order"
            );
        }
    }
    private void rejectOtherOffers(CustomerOrder order, Long acceptedOfferId) {

        order.getOffers()
                .stream()
                .filter(offer ->
                        !offer.getId()
                                .equals(acceptedOfferId)
                )
                .forEach(offer ->
                        offer.setOfferStatus(
                                OfferStatus.REJECTED
                        ));
    }

    private void validateOfferCreation(Expert expert, CustomerOrder order) {

        if (expert.getAccountStatus() != AccountStatus.APPROVED) {
            throw new BadRequestException("Expert is not approved");
        }

        if (!expert.getHomeServices().contains(order.getHomeService())) {
            throw new BadRequestException("Expert is not assigned to this service");
        }

        if (offerRepository.existsByCustomerOrderIdAndExpertId(order.getId(), expert.getId())) {
            throw new BadRequestException("Offer already submitted");
        }

        OrderStatus status = order.getOrderStatus();
        if ( status != OrderStatus.WAITING_FOR_OFFERS
                && status != OrderStatus.WAITING_FOR_SELECTION) {
            throw new BadRequestException("Order is not available for offers");
        }
    }
}
