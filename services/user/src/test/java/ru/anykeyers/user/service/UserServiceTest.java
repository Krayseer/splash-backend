//package ru.anykeyers.user.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import ru.anykeyers.commonsapi.remote.RemoteStorageService;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Тесты для {@link UserService}
// */
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RoleRepository roleRepository;
//
//    @Mock
//    private RemoteStorageService remoteStorageService;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Captor
//    private ArgumentCaptor<User> captor;
//
//    /**
//     * Тест регистрации пользователя уже с существующим username
//     */
//    @Test
//    void registerUserWithAlreadyExistsUsername() {
//        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder().username("existing-user").build();
//        Mockito.when(userRepository.findUserByUsername("existing-user")).thenReturn(new User());
//        UserAlreadyExistsException exception = Assertions.assertThrows(
//                UserAlreadyExistsException.class, () -> userService.registerUser(userRegisterRequest)
//        );
//        Assertions.assertEquals("User already exists: existing-user", exception.getMessage());
//        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест успешной регистрации пользователя
//     */
//    @Test
//    void registerUser() {
//        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
//                .name("test-name")
//                .surname("test-surname")
//                .username("test-user")
//                .password("test-passwd")
//                .email("test-email")
//                .phoneNumber("test-phone-number")
//                .build();
//        Role role = new Role();
//        Mockito.when(userRepository.findUserByUsername("test-user")).thenReturn(null);
//        Mockito.when(roleRepository.findByRoleCode("ROLE_USER")).thenReturn(role);
//
//        userService.registerUser(userRegisterRequest);
//
//        Mockito.verify(userRepository).save(captor.capture());
//        User user = captor.getValue();
//        Assertions.assertEquals("test-name", user.getName());
//        Assertions.assertEquals("test-surname", user.getSurname());
//        Assertions.assertEquals("test-user", user.getUsername());
//        Assertions.assertEquals("test-email", user.getEmail());
//        Assertions.assertEquals("test-phone-number", user.getPhoneNumber());
//    }
//
//    /**
//     * Тест установки ролей несуществующему пользователю
//     */
//    @Test
//    void setRolesOnNotExistingUser() {
//        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
//        UserNotFoundException exception = Assertions.assertThrows(
//                UserNotFoundException.class, () -> userService.setUserRoles(1L, Collections.emptyList())
//        );
//        Assertions.assertEquals("User not found with id: 1", exception.getMessage());
//        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест успешной установки ролей пользователю
//     */
//    @Test
//    void setUserRoles() {
//        Long userId = 1L;
//        Role existingRole = new Role();
//        existingRole.setId(2L);
//        Role newRole = new Role();
//        newRole.setId(3L);
//        List<Role> existingRoles = new ArrayList<>();
//        existingRoles.add(existingRole);
//        List<String> newRoles = new ArrayList<>();
//        newRoles.add("ROLE_MANAGER");
//        User user = User.builder()
//                .id(userId)
//                .roleList(existingRoles)
//                .build();
//        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        Mockito.when(roleRepository.findByRoleCodeIn(newRoles)).thenReturn(List.of(newRole));
//
//        userService.setUserRoles(userId, newRoles);
//
//        Mockito.verify(userRepository, Mockito.times(1)).save(captor.capture());
//        List<Role> exceptedRoles = new ArrayList<>();
//        exceptedRoles.add(newRole);
//        exceptedRoles.add(existingRole);
//        Assertions.assertEquals(exceptedRoles, captor.getValue().getRoleList());
//    }
//
//    /**
//     * Тест добавления фотографии пользователю
//     */
//    @Test
//    void addPhoto() {
//        String username = "test-user";
//        User user = User.builder().username(username).build();
//        Mockito.when(remoteStorageService.uploadPhoto(Mockito.any()))
//                .thenReturn(ResponseEntity.of(Optional.of("test-photo-uuid")));
//        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(user);
//
//        userService.addPhoto(username, null);
//
//        Mockito.verify(userRepository, Mockito.times(1)).save(captor.capture());
//        Assertions.assertEquals("test-photo-uuid", captor.getValue().getPhotoUrl());
//    }
//
//    /**
//     * Тест установки настроек пользователю
//     */
//    @Test
//    void setUserSetting() {
//        UserSettingDTO userSettingDTO = new UserSettingDTO(true, false);
//        UserSetting userSetting = new UserSetting();
//        User user = User.builder().userSetting(userSetting).build();
//        Mockito.when(userRepository.findUserByUsername("test-user")).thenReturn(user);
//
//        userService.setUserSetting("test-user", userSettingDTO);
//
//        Mockito.verify(userRepository, Mockito.times(1)).save(captor.capture());
//        User captorUser = captor.getValue();
//        Assertions.assertFalse(captorUser.getUserSetting().isEmailEnabled());
//        Assertions.assertTrue(captorUser.getUserSetting().isPushEnabled());
//    }
//
//}