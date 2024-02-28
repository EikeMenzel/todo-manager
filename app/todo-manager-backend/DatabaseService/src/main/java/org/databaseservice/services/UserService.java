package org.databaseservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.databaseservice.mapper.IUserMapper;
import org.databaseservice.payload.RegisterDTO;
import org.databaseservice.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsUserEntityByUsername(username);
    }

    @Override
    public boolean saveUser(RegisterDTO registerDTO) {
        try {
            userRepository.save(userMapper.registerDtoToUserEntity(registerDTO));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
