package com.example.washifyservice.controller;


import com.example.washifyservice.exception.ResourceNotFoundException;

import com.example.washifyservice.model.Device;
import com.example.washifyservice.repository.DeviceRepository;
import com.example.washifyservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users/{userId}/devices")
    public List<Device> getDevicesByUserId(@PathVariable Long userId) {
        return deviceRepository.findByUserId(userId);
    }

    @PostMapping("/users/{userId}/devices")
    public Device addDevice(@PathVariable Long userId,
                            @Valid @RequestBody Device device) {
        return userRepository.findById(userId)
                .map(user -> {
                    device.setUser(user);
                    return deviceRepository.save(device);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @PutMapping("/users/{userId}/devices/{deviceId}")
    public Device updateDevice(@PathVariable Long userId,
                               @PathVariable Long deviceId,
                               @Valid @RequestBody Device deviceRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Device not found with id " + userId);
        }

        return deviceRepository.findById(deviceId)
                .map(device -> {
                    device.setName(deviceRequest.getName());
                    device.setDeviceIdentifier(deviceRequest.getDeviceIdentifier());
                    device.setDeviceIdentifier(deviceRequest.getDeviceIdentifier());
                    device.setDeviceVersion(deviceRequest.getDeviceVersion());

                    return deviceRepository.save(device);
                }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));
    }

    @DeleteMapping("/users/{userId}/devices/{deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long userId,
                                          @PathVariable Long deviceId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }

        return deviceRepository.findById(deviceId)
                .map(device -> {
                    deviceRepository.delete(device);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Device not found with id " + deviceId));

    }

}
