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
import static org.osgi.framework.Bundle.ACTIVE;

import java.util.HashMap;
import java.util.Map;

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

    public static class GradleCoord {

        private final String LOCAL_GRADLE_CACHE = "file:" + System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1";

        public String hash;

        public String version;

        public GradleCoord(String hash, String version) {
            this.hash = hash;
            this.version = version;
        }

        public String tolocalGradleUrl(String artifactId) {
            return tolocalGradleUrl(VIRGO_MIRROR_GROUP_ID, artifactId);
        }

        String tolocalGradleUrl(String groupId, String artifactId) {
            return LOCAL_GRADLE_CACHE + "/" + groupId + "/" + artifactId + "/" + version + "/" + hash + "/" + artifactId + "-" + version
                + ".jar";
        }
    }

    protected static final Map<String, GradleCoord> gradleBundle = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringBundleResolutionTest.class);

    private static final String SPRINGFRAMEWORK_MAVEN_URL = "file:" + System.getProperty("user.home") + "/.m2/repository/";

    private static final String VIRGO_MIRROR_GROUP_ID = "org.eclipse.virgo.mirrored";

    private static final String SPRINGFRAMEWORK_VERSION = "4.2.4.RELEASE";

    static {
        // Spring Framework 4.2.4.Release hashes
        gradleBundle.put("org.springframework.aop", new GradleCoord("16f67328701199643ae2168dac35ed31df4621c1", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.beans", new GradleCoord("6a7d3b489e453ddee020baa7faba96eb7ce7d67e", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.context", new GradleCoord("6b8dc420fb4b829aabb92e4bab7f7daf82ab2db8", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.context.support", new GradleCoord("4d05d4d344d02d84287590a752547127acb4c1d", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.core", new GradleCoord("fa7c65339d5752ae7a03035e481f0e2829654c49", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.expression", new GradleCoord("396b218bebdeacd8a16ee0cd0a1c5b48174e3b23", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.jdbc", new GradleCoord("46cd9838a6095fd62c1930a6e0228bc7de317302", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.jms", new GradleCoord("d5c9abb9c9df65164a64bdfe649bc79d7f92262a", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.messaging", new GradleCoord("e4dc4dddfe432729481748b3814dfa497a1bf145", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.orm", new GradleCoord("d22e37c030eaea4e371ce69a53ab86e225e23c64", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.oxm", new GradleCoord("cae33b9c5aeb4ac2f1aced8e6224ff648366f484", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.transaction", new GradleCoord("981af42f54a6333d2dad5c9446e565239104b47b", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.web", new GradleCoord("829af385961f6c3bc4b605cbd6995de5d24d3f50", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.webmvc", new GradleCoord("7ed608501b13022a78fef2198dc92d5d1e950e36", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.webmvc.portlet", new GradleCoord("f383abc9d132702e2f8f021194ed43083882e48e", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.websocket", new GradleCoord("106b5641a225adc2a96fe047ae520ceca9ffad3c", SPRINGFRAMEWORK_VERSION));

        gradleBundle.put("oevm.org.aopalliance", new GradleCoord("87b10944aa0d2bb94e36c9489fc0b17fcf367bf0", "1.0.0"));
        gradleBundle.put("oevm.javax.portlet", new GradleCoord("83c32e38223e7dc072dec0e28eaea978f60eb15b/", "2.0.0"));
        gradleBundle.put("org.apache.commons.logging", new GradleCoord("16f574f7c054451477d7fc9d1f294e22b70a8eba", "1.2.0"));
        gradleBundle.put("org.apache.commons.codec", new GradleCoord("8aff50e99bd7e53f8c4f5fe45c2a63f1d47dd19c", "1.10.0"));
        gradleBundle.put("javax.jms", new GradleCoord("11cde07670acb2f0108b646e98a863a56be275e9", "1.1.0.v201205091237"));
        gradleBundle.put("javax.servlet", new GradleCoord("a7bfaab14cfbe1a6ea7a5d8a2e53f5a2e7cd32dd", "3.1.0.20150414"));
        gradleBundle.put("javax.websocket", new GradleCoord("c340de2a96aaca948a443793e2c7ce1740c1b387", "1.1.0.v201401130840"));
    }

    @Inject
    private BundleContext bundleContext;

    protected static Option provisionMirroredGradleBundle(String artifactId) {
        GradleCoord gradleCoord = gradleBundle.get(artifactId);
        if (gradleCoord == null) {
            throw new IllegalArgumentException("No Gradle coords available for '" + artifactId + "'");
        }
        return provision(bundle(gradleCoord.tolocalGradleUrl(artifactId)));
    }

    protected static Option provisionGradleBundle(String groupId, String artifactId) {
        GradleCoord gradleCoord = gradleBundle.get(artifactId);
        if (gradleCoord == null) {
            throw new IllegalArgumentException("No Gradle coords available for '" + artifactId + "'");
        }
        return provision(bundle(gradleCoord.tolocalGradleUrl(groupId, artifactId)));
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

    public void assertBundleIsActive(String bundleName) {
        boolean found = false;
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            String symbolicName = bundle.getSymbolicName();
            if (symbolicName.startsWith(bundleName)) {
                System.out.println("Found bundle '" + bundle.getSymbolicName() + "' with version: '" + bundle.getVersion() + "'.");
                found = true;
                if (ACTIVE != bundle.getState()) {
                    try {
                        LOG.error("Bundle found, but not active. Triggering start to get more information...");
                        bundle.start();
                    } catch (BundleException e) {
                        LOG.error("Error during start was:", e);
                    }
                }
                assertEquals(symbolicName + " is not ACTIVE", ACTIVE, bundle.getState());
            }
        }
        assertTrue("Spring Framework bundle not found", found);
    }
}
