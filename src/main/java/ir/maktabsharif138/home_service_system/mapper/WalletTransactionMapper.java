package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import ir.maktabsharif138.home_service_system.entity.WalletTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletTransactionMapper {

    @Mapping(target = "walletId", source = "wallet.id")
    WalletTransactionResponse toResponse(WalletTransaction transaction);


}