package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.response.WalletResponse;
import ir.maktabsharif138.home_service_system.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "expertId", source = "expert.id")
    WalletResponse toResponse(Wallet wallet);
}