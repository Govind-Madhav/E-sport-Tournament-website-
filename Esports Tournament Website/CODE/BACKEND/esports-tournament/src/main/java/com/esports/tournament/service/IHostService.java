package com.esports.tournament.service;

import com.esports.tournament.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IHostService {

    User registerHost(User host, MultipartFile imageFile) throws IOException;

    User loginHost(String email, String password);

    String forgotPassword(String email, String newPassword);

    List<User> getAllVerifiedHosts();

    List<User> getAllPendingHosts();

    List<User> getAllHosts();

    User approveHost(Long hostId);

    User getHostById(Long id);

    String deleteHost(Long id);
}
