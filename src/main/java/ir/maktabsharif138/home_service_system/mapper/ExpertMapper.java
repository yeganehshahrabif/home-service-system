package ir.maktabsharif138.home_service_system.mapper;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.entity.Expert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpertMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    Expert toExpert(ExpertRegisterRequest request);

    ExpertResponse toResponseExpert(Expert expert);

    void updateExpert(@MappingTarget Expert expert, ExpertUpdateRequest request);

    List<ExpertResponse> toExpertResponse(List<Expert> experts);
}
