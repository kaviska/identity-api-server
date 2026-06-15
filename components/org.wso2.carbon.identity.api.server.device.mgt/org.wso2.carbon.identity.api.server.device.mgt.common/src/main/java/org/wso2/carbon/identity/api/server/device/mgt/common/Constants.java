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

package org.wso2.carbon.identity.api.server.device.mgt.common;

/**
 * Constants for the device management REST API.
 */
public class Constants {

    private Constants() {
    }

    public static final String DEVICE_MGT_ERROR_PREFIX = "DM-";

    /**
     * Error messages for the device management API.
     */
    public enum ErrorMessage {

        ERROR_CODE_DEVICE_NOT_FOUND("60001",
                "Device not found.",
                "No device found for the given device id: %s."),

        ERROR_CODE_ERROR_LISTING_DEVICES("65001",
                "Unable to list devices.",
                "Server encountered an error while listing devices."),

        ERROR_CODE_ERROR_RETRIEVING_DEVICE("65002",
                "Unable to retrieve device.",
                "Server encountered an error while retrieving the device."),

        ERROR_CODE_ERROR_UPDATING_DEVICE("65003",
                "Unable to update device.",
                "Server encountered an error while updating the device."),

        ERROR_CODE_ERROR_DELETING_DEVICE("65004",
                "Unable to delete device.",
                "Server encountered an error while deleting the device."),

        ERROR_CODE_ERROR_LISTING_DEVICES_BY_USER("65005",
                "Unable to list devices for user.",
                "Server encountered an error while listing devices for the given user.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        /**
         * Returns the error code with prefix.
         *
         * @return Error code.
         */
        public String code() {

            return DEVICE_MGT_ERROR_PREFIX + code;
        }

        /**
         * Returns the error message.
         *
         * @return Error message.
         */
        public String message() {

            return message;
        }

        /**
         * Returns the error description.
         *
         * @return Error description.
         */
        public String description() {

            return description;
        }
    }
}
