package org.thingsboard.api.v2.controller;

import org.springframework.web.bind.annotation.*;
import org.thingsboard.api.core.dto.ApiResponse;
import org.thingsboard.api.v2.dto.DeviceV2;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/v2/devices")
@RequiredArgsConstructor
public class DeviceControllerV2 {

    private static final String VERSION = "v2";

    @GetMapping
    public ApiResponse<List<DeviceV2>> getAllDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // V2: Added pagination support
        List<DeviceV2> devices = List.of(
            DeviceV2.builder().id("1").name("Device 1").type("sensor").status("active").build(),
            DeviceV2.builder().id("2").name("Device 2").type("gateway").status("active").build()
        );
        return ApiResponse.success(devices, VERSION);
    }

    @GetMapping("/{id}")
    public ApiResponse<DeviceV2> getDevice(@PathVariable String id) {
        // V2: Added more fields (status, metadata)
        DeviceV2 device = DeviceV2.builder()
                .id(id)
                .name("Device " + id)
                .type("sensor")
                .status("active")
                .build();
        return ApiResponse.success(device, VERSION);
    }

    @PostMapping
    public ApiResponse<DeviceV2> createDevice(@RequestBody DeviceV2 device) {
        return ApiResponse.success(device, VERSION);
    }

    @PatchMapping("/{id}")
    public ApiResponse<DeviceV2> patchDevice(
            @PathVariable String id,
            @RequestBody DeviceV2 device) {
        // V2: Added PATCH support
        device.setId(id);
        return ApiResponse.success(device, VERSION);
    }
}
