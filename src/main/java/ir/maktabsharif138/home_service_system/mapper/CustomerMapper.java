package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.CustomerRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.CustomerUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerResponse;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;
import ir.maktabsharif138.home_service_system.dto.response.UserSearchResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    Customer toCustomer(CustomerRegisterRequest request);

    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);

    CustomerResponse toCustomerResponse(Customer customer);

    LoginResponse toLoginResponse(Customer customer);

    @Mapping(target = "rating", ignore = true)
    UserSearchResponse toSearchResponse(Customer customer);
}
