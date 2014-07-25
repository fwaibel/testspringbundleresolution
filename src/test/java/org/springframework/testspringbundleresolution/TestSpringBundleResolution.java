/*
 * Copyright 2011 VMware Inc.
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
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;

import javax.inject.Inject;

import org.ops4j.pax.exam.junit.PaxExam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(PaxExam.class)
public class TestSpringBundleResolution {

    private static final String SPRINGFRAMEWORK_RELEASE_MAVEN_URL = "http://repository.springsource.com/maven/bundles/release";

    private static final String EBR_EXTERNAL_MAVEN_URL = "http://repository.springsource.com/maven/bundles/external";

    private static final String SPRING_VERSION = "3.1.4.RELEASE";

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(//
            provisionSpringBundle("org.springframework.core"), //
            provisionSpringBundle("org.springframework.beans"), //
            provisionSpringBundle("org.springframework.aop"), //
            provisionSpringBundle("org.springframework.asm"), //
            provisionSpringBundle("org.springframework.aspects"), //
            provisionSpringBundle("org.springframework.context"), //
            provisionSpringBundle("org.springframework.context.support"), //
            provisionSpringBundle("org.springframework.expression"), //
            provisionSpringBundle("org.springframework.jdbc"), //
            provisionSpringBundle("org.springframework.jms"), //
            provisionSpringBundle("org.springframework.orm"), //
            provisionSpringBundle("org.springframework.oxm"), //
            provisionSpringBundle("org.springframework.transaction"), //
            provisionSpringBundle("org.springframework.web"), //
            provisionSpringBundle("org.springframework.web.servlet"), //
            provisionSpringBundle("org.springframework.web.portlet"), //
            
            // mandatory dependencies common to multiple Spring bundles
            provisionExternalBundle("org.apache.commons", "com.springsource.org.apache.commons.logging", "1.1.1"), //
            
            // spring-aop mandatory dependencies
            provisionExternalBundle("org.aopalliance", "com.springsource.org.aopalliance", "1.0.0"), //

            // spring-jms mandatory dependencies
            provisionExternalBundle("javax.jms", "com.springsource.javax.jms", "1.1.0"), //

            // spring-web mandatory dependencies
            provisionExternalBundle("javax.servlet", "javax.servlet", "3.0.0.v201103241009"), //

            // spring-webmvc-portlet mandatory dependencies
            provisionExternalBundle("javax.portlet", "com.springsource.javax.portlet", "2.0.0.v20110525"), //

            junitBundles());
    }

    private static Option provisionExternalBundle(String groupId, String artifactId, String version) {
        return provision(bundle(mavenUrl(EBR_EXTERNAL_MAVEN_URL, groupId, artifactId, version)));
    }

    private static String mavenUrl(String repositoryUrl, String groupId, String artifactId, String version) {
        // Use Pax URL. See http://team.ops4j.org/wiki/display/paxurl/Mvn+Protocol
        return "mvn:" + repositoryUrl + "!" + groupId + "/" + artifactId + "/" + version + "/" + "jar";
    }
    
    private static Option provisionSpringBundle(String artifactId) {
        return provision(bundle(springBundleUrl(artifactId)));
    }

    private static String springBundleUrl(String artifactId) {
        String version = SPRING_VERSION;
        String groupId = "org.springframework";
        String repositoryUrl = SPRINGFRAMEWORK_RELEASE_MAVEN_URL;
        return plainMavenUrl(repositoryUrl, groupId, artifactId, version);
    }
    
    private static String plainMavenUrl(String repositoryUrl, String groupId, String artifactId, String version) {
        return repositoryUrl + "/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
    }

    @Test
    public void testSpringCoreIsResolvable() throws Exception {
        int found = 0;
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            String symbolicName = bundle.getSymbolicName();
            if (symbolicName.startsWith("org.springframework")) {
                found++;
                assertEquals(symbolicName + " is not ACTIVE", Bundle.ACTIVE, bundle.getState());
            }
        }
        assertEquals("Unexpected number of Spring bundles found", 16, found);
    }
}