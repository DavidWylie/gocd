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

package com.thoughtworks.go.api.util;

import spark.HaltException;

import static spark.Spark.halt;

public abstract class HaltApiResponses {

    public static HaltException haltBecauseUnauthorized() {
        return halt(401, MessageJson.create(HaltApiMessages.unauthorizedMessage()));
    }

    public static HaltException haltBecauseEntityAlreadyExists(Object jsonInRequestBody, String entityType, Object existingName) {
        return halt(422, MessageJson.create(HaltApiMessages.entityAlreadyExistsMessage(entityType, existingName), jsonInRequestBody));
    }

    public static HaltException haltBecauseRenameOfEntityIsNotSupported(String entityType) {
        return halt(422, MessageJson.create(HaltApiMessages.renameOfEntityIsNotSupportedMessage(entityType)));
    }

    public static HaltException haltBecauseEtagDoesNotMatch(String entityType, Object name) {
        return halt(412, MessageJson.create(HaltApiMessages.etagDoesNotMatch(entityType, name)));
    }

    public static HaltException haltBecauseInvalidJSON(String errorMessage) {
        return halt(422, MessageJson.create(errorMessage));
    }

}
