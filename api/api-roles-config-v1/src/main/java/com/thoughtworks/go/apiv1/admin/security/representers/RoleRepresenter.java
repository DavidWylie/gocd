/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.apiv1.admin.security.representers;


import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.api.representers.ErrorGetter;
import com.thoughtworks.go.api.representers.JsonReader;
import com.thoughtworks.go.api.representers.JsonWriter;
import com.thoughtworks.go.config.CaseInsensitiveString;
import com.thoughtworks.go.config.PluginRoleConfig;
import com.thoughtworks.go.config.Role;
import com.thoughtworks.go.config.RoleConfig;
import com.thoughtworks.go.spark.RequestContext;

import java.util.Collections;
import java.util.Map;

import static com.thoughtworks.go.api.util.HaltApiResponses.haltBecauseInvalidJSON;


public class RoleRepresenter {

    private static void addLinks(Role model, JsonWriter jsonWriter) {
        jsonWriter.addDocLink("https://api.gocd.org/#roles");
        jsonWriter.addLink("self", "/go/api/admin/security/roles/${role_name}", ImmutableMap.of("role_name", model.getName()));
        jsonWriter.addLink("find", "/go/api/admin/security/roles/:role_name");
    }

    public static Map toJSON(Role role, RequestContext requestContext) {
        if (role == null) return null;

        JsonWriter jsonWriter = new JsonWriter(requestContext);

        addLinks(role, jsonWriter);

        jsonWriter.add("name", role.getName().toString());
        jsonWriter.add("type", getRoleType(role));
        if (role.hasErrors()) {
            jsonWriter.add("errors", new ErrorGetter(Collections.singletonMap("authConfigId", "auth_config_id"))
                    .apply(role, requestContext));
        }
        if (role instanceof RoleConfig) {
            jsonWriter.add("attributes", GoCDRoleConfigRepresenter.toJSON((RoleConfig) role, requestContext));
        } else if (role instanceof PluginRoleConfig) {
            jsonWriter.add("attributes", PluginRoleConfigRepresenter.toJSON((PluginRoleConfig) role, requestContext));
        }
        return jsonWriter.getAsMap();
    }

    public static Role fromJSON(JsonReader jsonReader) {
        Role model;
        String type = jsonReader.optString("type").orElse("");

        if ("gocd".equals(type)) {
            model = GoCDRoleConfigRepresenter.fromJSON(jsonReader.readJsonObject("attributes"));
        } else if ("plugin".equals(type)) {
            model = PluginRoleConfigRepresenter.fromJSON(jsonReader.readJsonObject("attributes"));
        } else {
            throw haltBecauseInvalidJSON("Invalid role type %s. It has to be one of 'gocd' or 'plugin'");
        }

        model.setName(new CaseInsensitiveString(jsonReader.optString("name").orElse(null)));

        return model;
    }

    private static String getRoleType(Role role) {
        if (role instanceof RoleConfig) {
            return "gocd";
        } else {
            return "plugin";
        }
    }


}
