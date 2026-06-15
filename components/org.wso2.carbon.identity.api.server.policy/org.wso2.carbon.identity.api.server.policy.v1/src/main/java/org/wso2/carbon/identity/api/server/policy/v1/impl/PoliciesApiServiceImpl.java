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

package org.wso2.carbon.identity.api.server.policy.v1.impl;

import org.wso2.carbon.identity.api.server.policy.v1.PoliciesApiService;
import org.wso2.carbon.identity.api.server.policy.v1.core.PolicyService;
import org.wso2.carbon.identity.api.server.policy.v1.factories.PolicyServiceFactory;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Policies API service.
 */
public class PoliciesApiServiceImpl implements PoliciesApiService {

    private final PolicyService devicePolicyService;

    public PoliciesApiServiceImpl() {

        try {
            this.devicePolicyService = PolicyServiceFactory.getPolicyService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating PolicyService.", e);
        }
    }

    @Override
    public Response addPolicy(PolicyRequest policyRequest) {

        PolicyResponse policyResponse = devicePolicyService.addPolicy(policyRequest);
        return Response.status(Response.Status.CREATED).entity(policyResponse).build();
    }

    @Override
    public Response getPolicies() {

        return Response.ok().entity(devicePolicyService.getPolicies()).build();
    }

    @Override
    public Response getPolicyMetadata(String platform) {

        return Response.ok().entity(devicePolicyService.getMetadata(platform)).build();
    }

    @Override
    public Response deletePolicy(String policyId) {

        devicePolicyService.deletePolicy(policyId);
        return Response.noContent().build();
    }

    @Override
    public Response getPolicyById(String policyId) {

        PolicyResponse policyResponse = devicePolicyService.getPolicyById(policyId);
        return Response.ok().entity(policyResponse).build();
    }

    @Override
    public Response updatePolicy(String policyId, PolicyRequest policyRequest) {

        PolicyResponse policyResponse = devicePolicyService.updatePolicy(policyId, policyRequest);
        return Response.ok().entity(policyResponse).build();
    }
}
