package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.HomeServiceCreateRequest;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.HomeServiceResponse;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HomeServiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentService", ignore = true)
    @Mapping(target = "subServices", ignore = true)
    @Mapping(target = "experts", ignore = true)
    HomeService toHomeService(HomeServiceCreateRequest request);

    @Mapping(
            target = "parentServiceId",
            source = "parentService.id"
    )
    HomeServiceResponse toHomeServiceResponse(HomeService entity);

    void updateHomeService(@MappingTarget HomeService homeService, HomeServiceUpdateRequest request);


    List<HomeServiceResponse> toHomeServiceResponse(List<HomeService> homeServices);
}
