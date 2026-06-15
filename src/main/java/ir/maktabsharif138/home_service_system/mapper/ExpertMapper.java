package ir.maktabsharif138.home_service_system.mapper;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;
import ir.maktabsharif138.home_service_system.dto.response.UserSearchResponse;
import ir.maktabsharif138.home_service_system.entity.Expert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExpertMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    Expert toExpert(ExpertRegisterRequest request);

    ExpertResponse toExpertResponse(Expert expert);

    @Mapping(target = "role", expression = "java(expert.getRole().name())")
    LoginResponse toLoginResponse(Expert expert);

    void updateExpert(@MappingTarget Expert expert, ExpertUpdateRequest request);

    List<ExpertResponse> toExpertResponse(List<Expert> experts);

    UserSearchResponse toSearchResponse(Expert expert);
}
