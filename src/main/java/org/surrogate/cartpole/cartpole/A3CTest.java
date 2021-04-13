/* *****************************************************************************
 * Copyright (c) 2015-2019 Skymind, Inc.
 * Copyright (c) 2020 Konduit K.K.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/
package org.surrogate.cartpole.cartpole;

import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscrete;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscreteDense;
import org.deeplearning4j.rl4j.network.ac.ActorCriticFactorySeparateStdDense;
import org.deeplearning4j.rl4j.policy.ACPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.nd4j.linalg.learning.config.Adam;

import java.io.IOException;
import java.util.logging.Logger;

public class A3CTest {

    public static void main(String[] args) throws IOException {
        A3CSimple();
    }

    private static void A3CSimple() throws IOException {
        //define the mdp from gym (name, render)
        CartpoleNative mdp = new CartpoleNative(1);

        A3CDiscrete.A3CConfiguration CARTPOLE_A3C =
            A3CDiscrete.A3CConfiguration.builder()
                .seed(123)
                .maxEpochStep(200)
                .maxStep(5000)
                .numThread(3)
                .nstep(20)
                .updateStart(10)
                .rewardFactor(0.01)
                .gamma(0.99)
                .errorClamp(1.0)
            .build();

        ActorCriticFactorySeparateStdDense.Configuration CARTPOLE_NET_A3C =  ActorCriticFactorySeparateStdDense.Configuration
            .builder()
            .updater(new Adam(1e-2))
            .l2(0)
            .numHiddenNodes(16)
            .numLayer(3)
            .build();

        //define the training
        A3CDiscreteDense<Box> a3c = new A3CDiscreteDense<>(mdp, CARTPOLE_NET_A3C, CARTPOLE_A3C);

        a3c.train(); //start the training
        mdp.close();

        ACPolicy<Box> pol = a3c.getPolicy();

        pol.save("/tmp/simpleval1/", "/tmp/simplepol1");

        //reload the policy, will be equal to "pol", but without the randomness
        ACPolicy<Box> pol2 = ACPolicy.load("/tmp/simpleval1/", "/tmp/simplepol1");
        loadPolicy(pol2);
        System.out.println("sample finished.");
    }

    // pass in a generic policy and endID to allow access from other samples in this package..
    static void loadPolicy(ACPolicy<Box> pol) {
        //use the trained agent on a new similar mdp (but render it this time)

        //define the mdp from gym (name, render)
        CartpoleNative mdp2 = new CartpoleNative(1);

        //evaluate the agent
        double rewards = 0;
        for (int i = 0; i < 10; i++) {
            mdp2.reset();
            double reward = pol.play(mdp2);
            rewards += reward;
            Logger.getAnonymousLogger().info("Reward: " + reward);
        }

        Logger.getAnonymousLogger().info("average: " + rewards/1000);
        mdp2.close();
    }

}
