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

public class SpringDataCommonsResolutionTest extends AbstractSpringBundleResolutionTest {

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(//
            provisionMirroredGradleBundle("org.springframework.aop"), //
            provisionMirroredGradleBundle("org.springframework.beans"), //
            provisionMirroredGradleBundle("org.springframework.core"), //

        // mandatory dependencies common to multiple Spring bundles
            provisionMirroredGradleBundle("org.apache.commons.logging"), //
            provisionMirroredGradleBundle("org.apache.commons.codec"), //

        // mandatory dependencies for o.s.aop
            provisionMirroredGradleBundle("oevm.org.aopalliance"), //

            provision(mavenBundle("org.springframework.data", "spring-data-commons", "1.11.1.RELEASE")),

        junitBundles());
    }

    @Test
    public void springBundleShouldBeActive() throws Exception {
        assertBundleIsActive("org.springframework.core");
        assertBundleIsActive("org.springframework.beans");
        assertBundleIsActive("org.springframework.aop");

        assertBundleIsActive("org.springframework.data.core");
    }
}