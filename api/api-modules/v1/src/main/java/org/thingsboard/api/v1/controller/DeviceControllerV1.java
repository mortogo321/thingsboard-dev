package org.thingsboard.api.v1.controller;

import org.springframework.web.bind.annotation.*;
import org.thingsboard.api.core.dto.ApiResponse;
import org.thingsboard.api.v1.dto.DeviceV1;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
public class DeviceControllerV1 {

    private static final String VERSION = "v1";

    @GetMapping
    public ApiResponse<List<DeviceV1>> getAllDevices() {
        // TODO: Implement service layer
        List<DeviceV1> devices = List.of(
            new DeviceV1("1", "Device 1", "sensor"),
            new DeviceV1("2", "Device 2", "gateway")
        );
        return ApiResponse.success(devices, VERSION);
    }

    @GetMapping("/{id}")
    public ApiResponse<DeviceV1> getDevice(@PathVariable String id) {
        // TODO: Implement service layer
        DeviceV1 device = new DeviceV1(id, "Device " + id, "sensor");
        return ApiResponse.success(device, VERSION);
    }

    @PostMapping
    public ApiResponse<DeviceV1> createDevice(@RequestBody DeviceV1 device) {
        // TODO: Implement service layer
        return ApiResponse.success(device, VERSION);
    }
}
