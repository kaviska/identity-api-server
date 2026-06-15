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

public class Error {

    private String code;
    private String message;
    private String description;
    private String traceId;

    @ApiModelProperty(example = "DM-60001")
    @JsonProperty("code")
    @Valid
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @ApiModelProperty(example = "Some Error Message.")
    @JsonProperty("message")
    @Valid
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @ApiModelProperty(example = "Some Error Description.")
    @JsonProperty("description")
    @Valid
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @ApiModelProperty(example = "e0fbcfeb-3617-43c4-8dd0-7b7d38e13047")
    @JsonProperty("traceId")
    @Valid
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return Objects.equals(this.code, error.code);
    }

    @Override
    public int hashCode() { return Objects.hash(code); }

    @Override
    public String toString() {
        return "class Error {\n    code: " + code + "\n    message: " + message + "\n}";
    }
}
