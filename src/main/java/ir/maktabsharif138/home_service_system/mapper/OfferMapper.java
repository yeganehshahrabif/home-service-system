package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.OfferCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.OfferResponse;
import ir.maktabsharif138.home_service_system.entity.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OfferMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offerDate", ignore = true)
    @Mapping(target = "offerStatus", ignore = true)
    @Mapping(target = "customerOrder", ignore = true)
    @Mapping(target = "expert", ignore = true)
    Offer toOffer(OfferCreateRequest request);

    @Mapping(
            target = "expertName",
            expression =
                    "java(offer.getExpert().getFirstName() + \" \" + offer.getExpert().getLastName())"
    )
    @Mapping(
            target = "expertRating",
            source = "expert.rating"
    )
    @Mapping(
            target = "expertId",
            source = "expert.id"
    )
    OfferResponse toOfferResponse(Offer offer);

    void updateOffer(@MappingTarget Offer offer, OfferCreateRequest request);

    List<OfferResponse> toOfferResponse(List<Offer> offers);
}
