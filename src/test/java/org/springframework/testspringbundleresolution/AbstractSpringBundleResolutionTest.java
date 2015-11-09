/*
 * Copyright 2015 VMware Inc. and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.testspringbundleresolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.provision;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExam.class)
public abstract class AbstractSpringBundleResolutionTest {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringBundleResolutionTest.class);

    private static final String SPRINGFRAMEWORK_MAVEN_URL = "file:" + System.getProperty("user.home") + "/.m2/repository/";

    private static final String VIRGO_MIRROR_GROUP_ID = "org.eclipse.virgo.mirrored";

    // TODO change to spring.io / pivotal.io
    private static final String EBR_EXTERNAL_MAVEN_URL = "http://repository.springsource.com/maven/bundles/external";

    private static final String SPRINGFRAMEWORK_VERSION = "4.2.1.RELEASE";

    @Inject
    private BundleContext bundleContext;

    protected static Option provisionExternalBundle(String groupId, String artifactId, String version) {
        return provision(bundle(mavenUrl(EBR_EXTERNAL_MAVEN_URL, groupId, artifactId, version)));
    }
    
    private static String mavenUrl(String repositoryUrl, String groupId, String artifactId, String version) {
        // Use Pax URL. See m
        return "mvn:" + repositoryUrl + "!" + groupId + "/" + artifactId + "/" + version + "/" + "jar";
    }

    protected static Option provisionSpringBundle(String artifactId) {
        return provision(bundle(provisionLocalMirrorBundle(artifactId, SPRINGFRAMEWORK_VERSION)));
    }

    protected static Option provisionMirroredBundle(String artifactId, String version) {
        return provision(bundle(provisionLocalMirrorBundle(artifactId, version)));
    }

    private static String provisionLocalMirrorBundle(String artifactId, String version) {
        return plainMavenUrl(SPRINGFRAMEWORK_MAVEN_URL, VIRGO_MIRROR_GROUP_ID, artifactId, version);
    }
    
    private static String plainMavenUrl(String repositoryUrl, String groupId, String artifactId, String version) {
        return repositoryUrl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }

    public void assertSpringBundleIsActive(String springBundleName) {
        boolean found = false;
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            String symbolicName = bundle.getSymbolicName();
            if (symbolicName.startsWith(springBundleName)) {
                System.out.println("Found Spring bundle '" + bundle.getSymbolicName() + "' with version: '" + bundle.getVersion() + "'.");
                found = true;
                if (Bundle.ACTIVE != bundle.getState()) {
                    try {
                        LOG.error("Bundle found, but not active. Triggering start to get more information...");
                        bundle.start();
                    } catch (BundleException e) {
                        LOG.error("Error during start was:", e);
                    }
                }
                assertEquals(symbolicName + " is not ACTIVE", Bundle.ACTIVE, bundle.getState());
            }
        }
        assertTrue("Spring Framework bundle not found", found);
    }
}