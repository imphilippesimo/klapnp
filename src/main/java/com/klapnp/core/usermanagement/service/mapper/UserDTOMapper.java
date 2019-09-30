package com.klapnp.core.usermanagement.service.mapper;

import com.klapnp.core.usermanagement.model.KlapUser;
import com.klapnp.core.usermanagement.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserDTOMapper {

    public static UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    public abstract UserDTO UserToUserDTO(KlapUser klapUser);

    public abstract KlapUser UserDTOToUser(UserDTO userDTO);

    public abstract List<UserDTO> UsersToUserDTOs(List<KlapUser> users);

    public abstract List<KlapUser> UserDTOsToUsers(List<UserDTO> userDTOs);


}
