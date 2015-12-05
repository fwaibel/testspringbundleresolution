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

package org.springframework.testspringdatabundleresolution;

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

    private static class GradleCoord {

        private final String LOCAL_GRADLE_CACHE = "file:" + System.getProperty("user.home") + "/.gradle/caches/modules-2/files-2.1";

        public String hash;

        public String version;

        public GradleCoord(String hash, String version) {
            this.hash = hash;
            this.version = version;
        }

        String tolocalGradleUrl(String artifactId) {
            return tolocalGradleUrl(VIRGO_MIRROR_GROUP_ID, artifactId);
        }

        String tolocalGradleUrl(String groupId, String artifactId) {
            return LOCAL_GRADLE_CACHE + "/" + groupId + "/" + artifactId + "/" + version + "/" + hash + "/" + artifactId + "-" + version
                + ".jar";
        }
    }

    private static final Map<String, GradleCoord> gradleBundle = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringBundleResolutionTest.class);

    private static final String SPRINGFRAMEWORK_MAVEN_URL = "file:" + System.getProperty("user.home") + "/.m2/repository/";

    private static final String VIRGO_MIRROR_GROUP_ID = "org.eclipse.virgo.mirrored";

    private static final String SPRINGFRAMEWORK_VERSION = "4.2.1.RELEASE";

    static {
        // Spring Framework 4.2.1.Release hashes
        gradleBundle.put("org.springframework.aop", new GradleCoord("1cd3a6bb1a57e579a2e0b997f1ae1e681d14a527", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.beans", new GradleCoord("536cf3ea4dc7b3ec28dbd5f64fe964a5162770cf", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.context", new GradleCoord("bfe786ced2757e2e5abf1c34ccfe9214d0f8934", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.context.support", new GradleCoord("7d1873505d1fd35acf14424c613f19badf5cb230", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.core", new GradleCoord("87307bac631006c6b6b7a37714a21bd30a65805d", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.expression", new GradleCoord("142bd8bb19983689199324c3e089e6fbe9ecafa1", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.jdbc", new GradleCoord("41ccb766ca06a0b5fdaab6468bb8d916bdd1be84", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.jms", new GradleCoord("b91535c5ed6e0460a9d924bc178fdc8ff395e1a9", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.messaging", new GradleCoord("e4210a140a78a077381eb907b6d7b9cfe20c392", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.orm", new GradleCoord("171459e2169dda2ff325ab5cc3e00fd8078dc5e6", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.oxm", new GradleCoord("bfeb9588a8fb906888ad1d50d22090686f6b42fc", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.transaction", new GradleCoord("e2dfc872ce68d0f5d3e33b85096d9df0c24d7300", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.web", new GradleCoord("a220b01e6ed724dc801d6aeefb41d656c23a808f", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.webmvc", new GradleCoord("d00b642cafa4c072c982b7fc10ea8cac92b0e04b", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.webmvc.portlet", new GradleCoord("42e4742cb3fbc684c34f74fe7f4e7f1106ad4422", SPRINGFRAMEWORK_VERSION));
        gradleBundle.put("org.springframework.websocket", new GradleCoord("4b85b0bbe4fd1aab11b1f0f64083a8782ff82336", SPRINGFRAMEWORK_VERSION));

        gradleBundle.put("com.springsource.org.aopalliance", new GradleCoord("386c79a41560b4bbb6015fdff45413f665fc5c96", "1.0.0"));
        gradleBundle.put("com.springsource.javax.portlet", new GradleCoord("19f26f88a6bcc9f8e972f26e09f521ac5bbfeb22", "2.0.0.v20110525"));
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
                System.out.println("Found Spring bundle '" + bundle.getSymbolicName() + "' with version: '" + bundle.getVersion() + "'.");
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
        assertTrue("Bundle '" + bundleName+ "' not found", found);
    }
}