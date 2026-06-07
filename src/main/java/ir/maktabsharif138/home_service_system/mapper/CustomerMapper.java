package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.CustomerRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.CustomerUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    Customer toCustomer(CustomerRegisterRequest request);

    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);

    CustomerResponse toCustomerResponse(Customer customer);
}
