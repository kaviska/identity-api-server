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

package org.wso2.carbon.identity.api.server.policy.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.api.server.policy.common.PolicyServiceHolder;
import org.wso2.carbon.identity.api.server.policy.v1.function.PolicyRequestToPolicy;
import org.wso2.carbon.identity.api.server.policy.v1.function.PolicyToPolicyResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.DevicePolicyField;
import org.wso2.carbon.identity.api.server.policy.v1.model.DevicePolicyFieldDefinition;
import org.wso2.carbon.identity.api.server.policy.v1.model.DevicePolicyLink;
import org.wso2.carbon.identity.api.server.policy.v1.model.DevicePolicyOperator;
import org.wso2.carbon.identity.api.server.policy.v1.model.DevicePolicyValue;
import org.wso2.carbon.identity.api.server.policy.v1.model.DevicePolicyValueObject;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListItem;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListLink;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.policy.management.api.exception.PolicyManagementException;
import org.wso2.carbon.identity.policy.management.api.model.Policy;
import org.wso2.carbon.identity.policy.management.api.model.PolicyBasicInfo;
import org.wso2.carbon.identity.policy.management.api.service.PolicyManagementService;
import org.wso2.carbon.identity.rule.metadata.api.exception.RuleMetadataException;
import org.wso2.carbon.identity.rule.metadata.api.model.FieldDefinition;
import org.wso2.carbon.identity.rule.metadata.api.model.FlowType;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsInputValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsReferenceValue;
import org.wso2.carbon.identity.rule.metadata.api.model.OptionsValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Core service for Device Policy API — handles CRUD operations and field metadata retrieval.
 */
public class PolicyService {

    private static final Log LOG = LogFactory.getLog(PolicyService.class);
    private static final int DEFAULT_LIMIT = 30;
    private static final int DEFAULT_OFFSET = 0;

    private final PolicyManagementService policyManagementService;

    public PolicyService(PolicyManagementService policyManagementService) {

        this.policyManagementService = policyManagementService;
    }

    /**
     * Create a new device policy.
     */
    public PolicyResponse addPolicy(PolicyRequest policyRequest) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Policy policy = new PolicyRequestToPolicy().apply(policyRequest);
            Policy createdPolicy = policyManagementService.addPolicy(policy, tenantDomain);
            return new PolicyToPolicyResponse().apply(createdPolicy);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_ADDING_POLICY);
        }
    }

    /**
     * Get a device policy by its ID.
     */
    public PolicyResponse getPolicyById(String policyId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Policy policy = policyManagementService.getPolicyById(policyId, tenantDomain);
            if (policy == null) {
                throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_POLICY_NOT_FOUND, policyId);
            }
            return new PolicyToPolicyResponse(loadFieldDisplayNamesMap()).apply(policy);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_POLICY);
        }
    }

    /**
     * Update an existing device policy.
     */
    public PolicyResponse updatePolicy(String policyId, PolicyRequest policyRequest) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            Policy policy = new PolicyRequestToPolicy(policyId).apply(policyRequest);
            Policy updatedPolicy = policyManagementService.updatePolicy(policy, tenantDomain);
            return new PolicyToPolicyResponse().apply(updatedPolicy);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_POLICY);
        }
    }

    /**
     * Delete a device policy by its ID.
     */
    public void deletePolicy(String policyId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            // The backend delete is idempotent: it silently no-ops when the policy does not exist,
            // so no explicit not-found handling is needed here.
            policyManagementService.deletePolicy(policyId, tenantDomain);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_DELETING_POLICY);
        }
    }

    /**
     * Get a paginated list of device policy summaries for the current tenant, optionally filtered by name.
     *
     * @param limit  Maximum number of records to return (defaults to 30 when null).
     * @param offset Number of records to skip (defaults to 0 when null).
     * @param filter Name substring filter; null or blank returns all policies.
     * @return Paginated policy list response.
     */
    public PolicyListResponse getPolicies(Integer limit, Integer offset, String filter) {

        int resolvedLimit = limit != null ? limit : DEFAULT_LIMIT;
        int resolvedOffset = offset != null ? offset : DEFAULT_OFFSET;
        validatePaginationParameters(resolvedLimit, resolvedOffset);

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            int totalResults = policyManagementService.getPolicyCount(tenantDomain, filter);
            List<PolicyBasicInfo> policies =
                    policyManagementService.getPolicies(tenantDomain, filter, resolvedOffset, resolvedLimit);

            List<PolicyListItem> items = policies.stream()
                    .map(this::toPolicyListItem)
                    .collect(Collectors.toList());

            List<PolicyListLink> links = Util.buildPaginationLinks(
                            resolvedLimit, resolvedOffset, totalResults, Constants.POLICY_PATH_COMPONENT, null, filter)
                    .entrySet().stream()
                    .map(link -> new PolicyListLink().rel(link.getKey()).href(link.getValue()))
                    .collect(Collectors.toList());

            return new PolicyListResponse()
                    .totalResults(totalResults)
                    .startIndex(resolvedOffset + 1)
                    .count(items.size())
                    .policies(items)
                    .links(links);
        } catch (PolicyManagementException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_POLICIES);
        }
    }

    private PolicyListItem toPolicyListItem(PolicyBasicInfo policy) {

        return new PolicyListItem()
                .id(policy.getId())
                .name(policy.getName())
                .self(ContextLoader.buildURIForBody(
                        Constants.V1_API_PATH_COMPONENT + Constants.POLICY_PATH_COMPONENT
                                + "/" + policy.getId()).toString());
    }

    private void validatePaginationParameters(int limit, int offset) {

        if (limit < 1 || offset < 0) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_INVALID_PAGINATION);
        }
    }

    /**
     * Returns device policy field metadata, optionally filtered to a specific platform.
     * Calls RuleMetadataService for field definitions and applies platform filter from device-fields.json.
     */
    public List<DevicePolicyFieldDefinition> getMetadata(String platform) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<FieldDefinition> allFields = PolicyServiceHolder.getRuleMetadataService()
                    .getExpressionMeta(FlowType.DEVICE_POLICY, tenantDomain);

            Map<String, List<String>> applicablePlatforms =
                    PolicyServiceHolder.getDeviceFieldMetadataService().getFieldApplicablePlatforms();

            return allFields.stream()
                    .filter(fd -> isApplicable(fd.getField().getName(), platform, applicablePlatforms))
                    .map(this::toApiModel)
                    .collect(Collectors.toList());
        } catch (RuleMetadataException e) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_METADATA, e);
        }
    }

    private boolean isApplicable(String fieldName, String platform,
                                 Map<String, List<String>> applicablePlatformsMap) {

        if (platform == null) {
            return true;
        }
        List<String> platforms = applicablePlatformsMap.get(fieldName);
        return platforms == null || platforms.contains(platform);
    }

    private Map<String, String> loadFieldDisplayNamesMap() {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<FieldDefinition> fields = PolicyServiceHolder.getRuleMetadataService()
                    .getExpressionMeta(FlowType.DEVICE_POLICY, tenantDomain);
            Map<String, String> result = new HashMap<>();
            for (FieldDefinition fd : fields) {
                result.put(fd.getField().getName(), fd.getField().getDisplayName());
            }
            return result;
        } catch (RuleMetadataException e) {
            LOG.error("Failed to retrieve field display names from rule metadata.", e);
            return Collections.emptyMap();
        }
    }

    private DevicePolicyFieldDefinition toApiModel(FieldDefinition fd) {

        DevicePolicyField field = new DevicePolicyField();
        field.setName(fd.getField().getName());
        field.setDisplayName(fd.getField().getDisplayName());

        List<DevicePolicyOperator> operators = new ArrayList<>();
        for (org.wso2.carbon.identity.rule.metadata.api.model.Operator op : fd.getOperators()) {
            DevicePolicyOperator apiOp = new DevicePolicyOperator();
            apiOp.setName(op.getName());
            apiOp.setDisplayName(op.getDisplayName());
            operators.add(apiOp);
        }

        DevicePolicyValue value = new DevicePolicyValue();
        org.wso2.carbon.identity.rule.metadata.api.model.Value coreValue = fd.getValue();
        value.setInputType(DevicePolicyValue.InputTypeEnum.fromValue(
                coreValue.getInputType().name().toLowerCase()));
        value.setValueType(DevicePolicyValue.ValueTypeEnum.fromValue(
                mapValueType(coreValue.getValueType())));

        if (coreValue instanceof OptionsInputValue) {
            List<DevicePolicyValueObject> values = new ArrayList<>();
            for (OptionsValue ov : ((OptionsInputValue) coreValue).getValues()) {
                DevicePolicyValueObject vo = new DevicePolicyValueObject();
                vo.setName(ov.getName());
                vo.setDisplayName(ov.getDisplayName());
                values.add(vo);
            }
            value.setValues(values);
        } else if (coreValue instanceof OptionsReferenceValue) {
            List<DevicePolicyLink> links = new ArrayList<>();
            for (org.wso2.carbon.identity.rule.metadata.api.model.Link l :
                    ((OptionsReferenceValue) coreValue).getLinks()) {
                DevicePolicyLink link = new DevicePolicyLink();
                link.setHref(l.getHref());
                link.setMethod(DevicePolicyLink.MethodEnum.fromValue(l.getMethod()));
                link.setRel(DevicePolicyLink.RelEnum.fromValue(l.getRel()));
                links.add(link);
            }
            value.setLinks(links);
        }

        DevicePolicyFieldDefinition result = new DevicePolicyFieldDefinition();
        result.setField(field);
        result.setOperators(operators);
        result.setValue(value);
        return result;
    }

    private String mapValueType(org.wso2.carbon.identity.rule.metadata.api.model.Value.ValueType valueType) {

        if (valueType == org.wso2.carbon.identity.rule.metadata.api.model.Value.ValueType.DATE_TIME) {
            return "date";
        }
        return valueType.name().toLowerCase();
    }
}
