package com.example.auth.mapper;

import com.example.auth.dto.AccountDto;
import com.example.auth.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(Account account);
}
