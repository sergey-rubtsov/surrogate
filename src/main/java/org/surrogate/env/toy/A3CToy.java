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
package org.surrogate.env.toy;

import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscrete;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.A3CDiscreteDense;
import org.deeplearning4j.rl4j.mdp.toy.HardDeteministicToy;
import org.deeplearning4j.rl4j.mdp.toy.HardToyState;
import org.deeplearning4j.rl4j.network.ac.ActorCriticFactorySeparateStdDense;
import org.deeplearning4j.rl4j.policy.ACPolicy;
import org.nd4j.linalg.learning.config.Adam;

import java.io.IOException;
import java.util.logging.Logger;

public class A3CToy {

    public static void main(String[] args) throws IOException {
        A3CSimple();
    }

    private static void A3CSimple() throws IOException {
        //define the mdp from gym (name, render)
        HardDeteministicToy mdp = new HardDeteministicToy();

        A3CDiscrete.A3CConfiguration CARTPOLE_A3C =
            A3CDiscrete.A3CConfiguration.builder()
                .seed(123)
                .maxEpochStep(1)
                .maxStep(18)
                .numThread(1)
                .nstep(18)
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
        A3CDiscreteDense<HardToyState> a3c = new A3CDiscreteDense<>(mdp, CARTPOLE_NET_A3C, CARTPOLE_A3C);

        a3c.train(); //start the training
        mdp.close();

        ACPolicy<HardToyState> pol = a3c.getPolicy();

        pol.save("/tmp/toyval1/", "/tmp/toypol1");

        //reload the policy, will be equal to "pol", but without the randomness
        ACPolicy<HardToyState> pol2 = ACPolicy.load("/tmp/toyval1/", "/tmp/simplepol1");
        loadPolicy(pol2);
        System.out.println("sample finished.");
    }

    // pass in a generic policy and endID to allow access from other samples in this package..
    static void loadPolicy(ACPolicy<HardToyState> pol) {
        HardDeteministicToy mdp2 = new HardDeteministicToy();
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
