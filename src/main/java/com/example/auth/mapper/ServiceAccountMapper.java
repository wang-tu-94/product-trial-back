package com.example.auth.mapper;

import com.example.auth.dto.ServiceAccountDto;
import com.example.auth.model.ServiceAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceAccountMapper {
    ServiceAccountDto toDto(ServiceAccount serviceAccount);
}
