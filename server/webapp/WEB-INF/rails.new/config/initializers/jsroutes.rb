# Copyright 2015 ThoughtWorks, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
##########################################################################
JsRoutes.setup do |config|
  config.prefix     = com.thoughtworks.go.util.SystemEnvironment.new.getWebappContextPath
  config.camel_case = true
  config.include    = [
    /analytics/,
    /^api_internal/,
    /^apiv\d/,
    /^admin_elastic_profile/,
    /^admin_status_report/
  ]
end
