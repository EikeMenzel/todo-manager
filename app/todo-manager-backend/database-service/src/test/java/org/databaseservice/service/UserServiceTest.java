package org.databaseservice.service;

import org.databaseservice.exceptions.SaveException;
import org.databaseservice.mapper.IUserMapper;
import org.databaseservice.models.UserEntity;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.databaseservice.repository.IUserRepository;
import org.databaseservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserMapper userMapper;

    @Test
    void existsUserByUsername_UsernameExists_ShouldReturnTrue() {
        String existingUsername = "existingUser";
        Mockito.when(userRepository.existsUserEntityByUsername(existingUsername)).thenReturn(true);

        boolean exists = userService.existsUserByUsername(existingUsername);

        assertTrue(exists);
        Mockito.verify(userRepository, Mockito.times(1)).existsUserEntityByUsername(existingUsername);
    }

    @Test
    void getUserFromUsername_UserExists_ShouldReturnUserDTO() {
        String existingUsername = "existingUser";
        UserEntity userEntity = new UserEntity(1L, existingUsername, "password");
        UserDTO userDTO = new UserDTO(1L, existingUsername, "password");

        Mockito.when(userRepository.getUserEntityByUsername(existingUsername)).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapper.userEntityToUserDTO(userEntity)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserFromUsername(existingUsername);

        assertTrue(result.isPresent());
        assertEquals(userDTO, result.get());
    }

    @Test
    void saveUser_Success_ShouldSaveUser() {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "password");
        UserEntity userEntity = new UserEntity(); // Initialize as per your constructor or setter methods

        Mockito.when(userMapper.registerDtoToUserEntity(registerDTO)).thenReturn(userEntity);

        userService.saveUser(registerDTO);

        Mockito.verify(userMapper, Mockito.times(1)).registerDtoToUserEntity(registerDTO);
        Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
    }

    @Test
    void saveUser_RepositoryThrowsException_ShouldThrowSaveException() {
        RegisterDTO registerDTO = new RegisterDTO("newUser", "password");
        UserEntity userEntity = new UserEntity(); // Initialize as per your constructor or setter methods

        Mockito.when(userMapper.registerDtoToUserEntity(registerDTO)).thenReturn(userEntity);
        Mockito.doThrow(new RuntimeException("Database error")).when(userRepository).save(userEntity);

        assertThrows(SaveException.class, () -> userService.saveUser(registerDTO),
                "Expected saveUser to throw SaveException, but it didn't");

        Mockito.verify(userMapper, Mockito.times(1)).registerDtoToUserEntity(registerDTO);
        Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
    }

}
