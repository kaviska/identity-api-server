/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.device.mgt.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;

public class DeviceResponse {

    private String id;
    private String userId;
    private String userName;
    private String deviceName;
    private String deviceModel;
    private String status;
    private String registeredAt;
    private String metadata;

    @ApiModelProperty(example = "74070bae-df8c-42bf-8754-5173c237c936", value = "The device UUID.")
    @JsonProperty("id")
    @Valid
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public DeviceResponse id(String id) { this.id = id; return this; }

    @ApiModelProperty(example = "kaviska", value = "The user identifier who owns the device.")
    @JsonProperty("userId")
    @Valid
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public DeviceResponse userId(String userId) { this.userId = userId; return this; }

    @ApiModelProperty(example = "john@example.com", value = "The username of the user who owns the device.")
    @JsonProperty("userName")
    @Valid
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public DeviceResponse userName(String userName) { this.userName = userName; return this; }

    @ApiModelProperty(example = "My iPhone", value = "The display name of the device.")
    @JsonProperty("deviceName")
    @Valid
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public DeviceResponse deviceName(String deviceName) { this.deviceName = deviceName; return this; }

    @ApiModelProperty(example = "iPhone 15 Pro", value = "The hardware model of the device.")
    @JsonProperty("deviceModel")
    @Valid
    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }
    public DeviceResponse deviceModel(String deviceModel) { this.deviceModel = deviceModel; return this; }

    @ApiModelProperty(example = "ACTIVE", value = "The current device status.")
    @JsonProperty("status")
    @Valid
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public DeviceResponse status(String status) { this.status = status; return this; }

    @ApiModelProperty(example = "2026-04-27T10:00:00.000Z", value = "The timestamp when the device was registered.")
    @JsonProperty("registeredAt")
    @Valid
    public String getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(String registeredAt) { this.registeredAt = registeredAt; }
    public DeviceResponse registeredAt(String registeredAt) { this.registeredAt = registeredAt; return this; }

    @ApiModelProperty(example = "{\"osVersion\":\"17.0\"}", value = "Additional metadata associated with the device.")
    @JsonProperty("metadata")
    @Valid
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public DeviceResponse metadata(String metadata) { this.metadata = metadata; return this; }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceResponse that = (DeviceResponse) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "class DeviceResponse {\n" +
                "    id: " + id + "\n" +
                "    userId: " + userId + "\n" +
                "    userName: " + userName + "\n" +
                "    deviceName: " + deviceName + "\n" +
                "    deviceModel: " + deviceModel + "\n" +
                "    status: " + status + "\n" +
                "    registeredAt: " + registeredAt + "\n" +
                "    metadata: " + metadata + "\n" +
                "}";
    }
}
