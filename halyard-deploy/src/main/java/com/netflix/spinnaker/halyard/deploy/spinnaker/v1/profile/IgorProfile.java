/*
 * Copyright 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.halyard.deploy.spinnaker.v1.profile;

import com.netflix.spinnaker.halyard.config.model.v1.node.DeploymentConfiguration;
import com.netflix.spinnaker.halyard.config.model.v1.node.Providers;
import com.netflix.spinnaker.halyard.config.model.v1.node.Webhooks;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.SpinnakerArtifact;
import com.netflix.spinnaker.halyard.deploy.spinnaker.v1.SpinnakerEndpoints;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IgorProfile extends SpringProfile {
  @Override
  public SpinnakerArtifact getArtifact() {
    return SpinnakerArtifact.IGOR;
  }

  @Override
  public ProfileConfig generateFullConfig(ProfileConfig config, DeploymentConfiguration deploymentConfiguration, SpinnakerEndpoints endpoints) {
    config = super.generateFullConfig(config, deploymentConfiguration, endpoints);
    Providers providers = deploymentConfiguration.getProviders();
    if (providers.getDockerRegistry().isEnabled()) {
      config.extendConfig(config.getPrimaryConfigFile(), "dockerRegistry.enabled: true");
    }

    Webhooks webhooks = deploymentConfiguration.getWebhooks();
    List<String> files = processRequiredFiles(webhooks);
    return config.extendConfig(config.getPrimaryConfigFile(), yamlToString(webhooks)).setRequiredFiles(files);
  }
}
