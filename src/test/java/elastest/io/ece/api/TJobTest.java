package elastest.io.ece.api;

import io.elastest.ece.model.CostModel;
import io.elastest.ece.model.TJob;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;

/**
 * Copyright (c) 2017. Zuercher Hochschule fuer Angewandte Wissenschaften
 * All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 * <p>
 * Created by Manu Perez on 25.08.17.
 */

public class TJobTest extends TestCase{

    @Test
    public void testEquals(){
        TJob tJob = new TJob("tjob", new HashMap());
        TJob tJob1 = new TJob("tjob", new HashMap());

        tJob.setMetadata(new HashMap<>());
        tJob1.setMetadata(new HashMap<>());

        assertNotSame(new Double(0), tJob);
        assertEquals(tJob, tJob1);

    }
}