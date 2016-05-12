/*
 * Copyright (c) 2015 EclipseSource.
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

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;

import org.junit.Test;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.springframework.testspringbundleresolution.AbstractSpringBundleResolutionTest;

public class SpringDataMongoDbResolutionTest extends AbstractSpringBundleResolutionTest {

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(//
            provisionMirroredGradleBundle("org.springframework.aop"), //
            provisionMirroredGradleBundle("org.springframework.beans"), //
            provisionMirroredGradleBundle("org.springframework.context"), //
            provisionMirroredGradleBundle("org.springframework.core"), //
            provisionMirroredGradleBundle("org.springframework.expression"), //
            provisionMirroredGradleBundle("org.springframework.transaction"), //

        // mandatory dependencies common to multiple Spring bundles
            provisionMirroredGradleBundle("org.apache.commons.logging"), //
            provisionMirroredGradleBundle("org.apache.commons.codec"), //

        // mandatory dependencies for o.s.aop
            provisionMirroredGradleBundle("oevm.org.aopalliance"), //

        // mandatory dependencies for o.s.data.mongodb
            provision(mavenBundle("com.fasterxml.jackson.core", "jackson-core", "2.6.3")),
            provision(mavenBundle("com.fasterxml.jackson.core", "jackson-annotations", "2.6.3")),
            provision(mavenBundle("com.fasterxml.jackson.core", "jackson-databind", "2.6.3")),
            provision(mavenBundle("com.google.guava", "guava", "13.0.1")), //
            provision(mavenBundle("org.mongodb", "mongo-java-driver", "2.13.0")), //
            provision(mavenBundle("ch.qos.logback", "logback-core", "1.1.3")), //
            provision(mavenBundle("ch.qos.logback", "logback-classic", "1.1.3")), //
            provision(mavenBundle("org.slf4j", "slf4j-api", "1.7.13")),

        // bundle under test
            provision(mavenBundle("org.springframework.data", "spring-data-commons", "1.11.1.RELEASE")),
            provision(mavenBundle("org.springframework.data", "spring-data-mongodb", "1.8.1.RELEASE")),

        junitBundles());
    }

    @Test
    public void springBundleShouldBeActive() throws Exception {
        assertBundleIsActive("org.springframework.context");
        assertBundleIsActive("org.springframework.transaction");

        assertBundleIsActive("com.fasterxml.jackson.core.jackson-core");
        assertBundleIsActive("com.fasterxml.jackson.core.jackson-annotations");
        assertBundleIsActive("com.fasterxml.jackson.core.jackson-databind");
        assertBundleIsActive("com.google.guava");
        assertBundleIsActive("org.mongodb.mongo-java-driver");

        assertBundleIsActive("org.springframework.data.core");
        assertBundleIsActive("org.springframework.data.mongodb");
    }
}
