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

package org.wso2.carbon.identity.api.server.device.mgt.v1;

import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DevicePatchRequest;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.DeviceResponse;
import org.wso2.carbon.identity.api.server.device.mgt.v1.model.Error;
import org.wso2.carbon.identity.api.server.device.mgt.v1.factories.DevicesApiServiceFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/devices")
@Api(description = "The devices API")
public class DevicesApi {

    private final DevicesApiService delegate;

    public DevicesApi() {

        this.delegate = DevicesApiServiceFactory.getDevicesApi();
    }

    @Valid
    @GET
    @Produces({ "application/json" })
    @ApiOperation(value = "List all registered devices in the tenant.", notes = "This API provides the capability to list all registered devices for the tenant.", response = DeviceResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Device Management", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceResponse.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response listDevices() {

        return delegate.listDevices();
    }

    @Valid
    @GET
    @Path("/user/{user-id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "List all devices registered by a specific user.", notes = "This API provides the capability to list all registered devices for a specific user.", response = DeviceResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Device Management", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceResponse.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response listDevicesByUserId(@ApiParam(value = "UUID of the user", required = true) @PathParam("user-id") String userId) {

        return delegate.listDevicesByUserId(userId);
    }

    @Valid
    @GET
    @Path("/{device-id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a registered device by ID.", notes = "This API provides the capability to retrieve a registered device by its ID.", response = DeviceResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Device Management", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getDevice(@ApiParam(value = "UUID of the registered device", required = true) @PathParam("device-id") String deviceId) {

        return delegate.getDevice(deviceId);
    }

    @Valid
    @PATCH
    @Path("/{device-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Rename a registered device.", notes = "This API provides the capability to update the display name of a registered device.", response = DeviceResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Device Management", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Response", response = DeviceResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateDeviceName(@ApiParam(value = "UUID of the registered device", required = true) @PathParam("device-id") String deviceId,
            @ApiParam(value = "", required = true) @Valid DevicePatchRequest patchRequest) {

        return delegate.updateDeviceName(deviceId, patchRequest);
    }

    @Valid
    @DELETE
    @Path("/{device-id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a registered device.", notes = "This API provides the capability to delete a registered device by its ID.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Device Management" })
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteDevice(@ApiParam(value = "UUID of the registered device", required = true) @PathParam("device-id") String deviceId) {

        return delegate.deleteDevice(deviceId);
    }
}
