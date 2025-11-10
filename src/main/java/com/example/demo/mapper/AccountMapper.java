package com.example.demo.mapper;

import com.example.demo.dto.AccountDto;
import com.example.demo.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(Account account);
}
