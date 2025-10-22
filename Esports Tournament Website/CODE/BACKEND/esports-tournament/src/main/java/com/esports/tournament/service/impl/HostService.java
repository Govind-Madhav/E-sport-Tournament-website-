package com.esports.tournament.service.impl;

import com.esports.tournament.model.Role;
import com.esports.tournament.model.User;
import com.esports.tournament.repo.IUserRepository;
import com.esports.tournament.service.IHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class HostService implements IHostService {

    @Autowired
    private IUserRepository userRepository;

    @Value("${files.directory}")
    private String uploadDirectory;

    @Override
    public User registerHost(User host, MultipartFile imageFile) throws IOException {
        if (userRepository.findByEmail(host.getEmail()).isPresent()) {
            throw new RuntimeException("Host already registered with this email.");
        }

        Path path = Path.of(uploadDirectory);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }

        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        Path targetPath = path.resolve(fileName);

        Files.copy(imageFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(fileName)
                .toUriString();

        host.setRole(Role.HOST);
        host.setVerified(false);
        host.setImageUrl(imageUrl);

        return userRepository.save(host);
    }

    @Override
    public User loginHost(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || !userOpt.get().getRole().equals(Role.HOST)) {
            throw new RuntimeException("Host not found or not authorized.");
        }

        User host = userOpt.get();

        if (!host.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password.");
        }

        if (!host.isVerified()) {
            throw new RuntimeException("Host account is not yet verified by admin.");
        }

        return host;
    }


    @Override
    public String forgotPassword(String email, String newPassword) {
        User host = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Host not found with email: " + email));

        if (!host.getRole().name().equals("HOST")) {
            throw new RuntimeException("User is not a Host");
        }

        host.setPassword(newPassword);
        userRepository.save(host);

        return "Password updated successfully for Host!";
    }


    @Override
    public List<User> getAllVerifiedHosts() {
        List<User> hosts = userRepository.findByRoleAndVerified(Role.HOST, true);
        if (hosts.isEmpty()) {
            throw new RuntimeException("No verified hosts found");
        }
        return hosts;
    }


    @Override
    public List<User> getAllPendingHosts() {
        List<User> hosts = userRepository.findByRoleAndVerified(Role.HOST, false);
        if (hosts.isEmpty()) {
            throw new RuntimeException("No pending hosts found");
        }
        return hosts;
    }

    @Override
    public List<User> getAllHosts() {
        List<User> hosts = userRepository.findByRole(Role.HOST);
        if (hosts.isEmpty()) {
            throw new RuntimeException("No hosts found");
        }
        return hosts;
    }


    @Override
    public User approveHost(Long hostId) {
        User host = userRepository.findById(hostId).orElseThrow(() -> new RuntimeException("Host not found"));
        host.setVerified(true);
        return userRepository.save(host);
    }

    @Override
    public User getHostById(Long id) {
        Optional<User> hostOpt = userRepository.findById(id);

        if (hostOpt.isEmpty() || !hostOpt.get().getRole().equals(Role.HOST)) {
            throw new RuntimeException("Host not found with ID: " + id);
        }

        return hostOpt.get();
    }

    @Override
    public String deleteHost(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Host not found");
        }
        userRepository.deleteById(id);
        return "Host deleted successfully";
    }
}


