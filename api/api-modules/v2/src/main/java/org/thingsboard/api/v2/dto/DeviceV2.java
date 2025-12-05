package org.thingsboard.api.v2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceV2 {

    private String id;
    private String name;
    private String type;
    private String status;           // V2: Added status field
    private Map<String, Object> metadata;  // V2: Added metadata field
    private Long createdAt;          // V2: Added timestamp
    private Long updatedAt;          // V2: Added timestamp
}
