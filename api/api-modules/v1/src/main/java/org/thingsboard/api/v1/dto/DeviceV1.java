package org.thingsboard.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceV1 {

    private String id;
    private String name;
    private String type;
}
