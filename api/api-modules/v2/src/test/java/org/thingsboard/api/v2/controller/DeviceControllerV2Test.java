package org.thingsboard.api.v2.controller;

import org.junit.jupiter.api.Test;
import org.thingsboard.api.v2.dto.DeviceV2;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceControllerV2Test {

    private final DeviceControllerV2 controller = new DeviceControllerV2();

    @Test
    void listReturnsVersionedEnvelopeWithStatus() {
        var response = controller.getAllDevices(0, 10);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getVersion()).isEqualTo("v2");
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData()).allMatch(d -> "active".equals(d.getStatus()));
    }

    @Test
    void getByIdEchoesId() {
        var response = controller.getDevice("7");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getId()).isEqualTo("7");
        assertThat(response.getData().getStatus()).isEqualTo("active");
    }

    @Test
    void patchOverridesPathId() {
        DeviceV2 payload = DeviceV2.builder().id("stale").name("Valve").build();

        var response = controller.patchDevice("fresh", payload);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getData().getId()).isEqualTo("fresh");
        assertThat(response.getData().getName()).isEqualTo("Valve");
    }
}
