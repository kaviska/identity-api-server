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
import javax.validation.constraints.NotNull;

public class DevicePatchRequest {

    private String deviceName;

    @ApiModelProperty(example = "My Work Phone", required = true, value = "The new display name for the device.")
    @JsonProperty("deviceName")
    @Valid
    @NotNull(message = "Property deviceName cannot be null.")
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public DevicePatchRequest deviceName(String deviceName) { this.deviceName = deviceName; return this; }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevicePatchRequest that = (DevicePatchRequest) o;
        return Objects.equals(this.deviceName, that.deviceName);
    }

    @Override
    public int hashCode() { return Objects.hash(deviceName); }

    @Override
    public String toString() {
        return "class DevicePatchRequest {\n    deviceName: " + deviceName + "\n}";
    }
}
