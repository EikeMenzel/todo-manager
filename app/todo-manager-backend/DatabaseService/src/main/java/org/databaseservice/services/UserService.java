package org.databaseservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.databaseservice.constants.ErrorMessage;
import org.databaseservice.exceptions.SaveException;
import org.databaseservice.mapper.IUserMapper;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.payload.UserDTO;
import org.databaseservice.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    @Transactional
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserEntityByUsername(username);
    }

    @Override
    @Transactional
    public void saveUser(RegisterDTO registerDTO) {
        try {
            userRepository.save(userMapper.registerDtoToUserEntity(registerDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SaveException(ErrorMessage.SAVE_ERROR);
        }
    }

    @Transactional
    public Optional<UserDTO> getUserFromUsername(String username) {
        return userRepository.getUserEntityByUsername(username)
                .map(userMapper::UserEntityToUserDTO);
    }
}
