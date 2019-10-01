package com.klapnp.core.usermanagement.exposition.vm.mapper;

import com.klapnp.core.usermanagement.exposition.vm.RegisterUserVM;
import com.klapnp.core.usermanagement.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {

    UserDTO vmToDTO(RegisterUserVM registerUserVM);
}
