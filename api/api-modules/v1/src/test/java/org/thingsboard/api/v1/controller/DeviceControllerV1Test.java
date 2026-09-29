package org.thingsboard.api.v1.controller;

import org.junit.jupiter.api.Test;
import org.thingsboard.api.v1.dto.DeviceV1;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceControllerV1Test {

    private final DeviceControllerV1 controller = new DeviceControllerV1();

    @Test
    void listReturnsVersionedEnvelope() {
        var response = controller.getAllDevices();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getVersion()).isEqualTo("v1");
        assertThat(response.getData()).hasSize(2);
    }

    @Test
    void getByIdEchoesId() {
        var response = controller.getDevice("42");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getId()).isEqualTo("42");
    }

    @Test
    void createEchoesPayload() {
        DeviceV1 device = new DeviceV1("9", "Pump", "sensor");

        var response = controller.createDevice(device);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData()).isEqualTo(device);
    }
}
