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

package org.springframework.testspringbundleresolution;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.junit.Test;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;

public class SpringFrameworkJmsResolutionTest extends AbstractSpringBundleResolutionTest {

    @Configuration
    public static Option[] configuration() throws Exception {
        return options(//
            
            provisionSpringBundle("org.springframework.aop"), //
            provisionSpringBundle("org.springframework.beans"), //
            provisionSpringBundle("org.springframework.context"), //
            provisionSpringBundle("org.springframework.core"), //
            provisionSpringBundle("org.springframework.expression"), //
            provisionSpringBundle("org.springframework.jms"), //
            provisionSpringBundle("org.springframework.messaging"), //
            provisionSpringBundle("org.springframework.oxm"), //
            provisionSpringBundle("org.springframework.transaction"), //

        // mandatory dependencies for o.s.core
            provisionMirroredBundle("org.apache.commons.logging", "1.2.0"), //
            provisionMirroredBundle("org.apache.commons.codec", "1.10.0"), //

            // mandatory dependencies for o.s.aop
            provisionMirroredBundle("org.aopalliance.aop", "1.0.0"), //
            
        // mandatory dependencies for o.s.jms
// mvn install:install-file -Dfile=javax.jms_1.1.0.v201205091237.jar -DgroupId=org.eclipse.virgo.mirror -DartifactId=javax.jms -Dversion=1.1.0.v201205091237 -Dpackaging=jar
            provisionMirroredBundle("javax.jms", "1.1.0.v201205091237"), //

        junitBundles());
    }

    @Test
    public void springBundleShouldBeActive() throws Exception {
        assertSpringBundleIsActive("org.springframework.jms");
    }
}