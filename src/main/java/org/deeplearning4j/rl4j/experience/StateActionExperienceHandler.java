/*******************************************************************************
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
package org.deeplearning4j.rl4j.experience;

import org.deeplearning4j.rl4j.observation.Observation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link ExperienceHandler experience handler} that stores the experiences.
 * Note: Calling {@link StateActionExperienceHandler#generateTrainingBatch() generateTrainingBatch()} will clear the stored experiences
 *
 * @param <A> Action type
 * @author Alexandre Boulanger
 */
public class StateActionExperienceHandler<A> implements ExperienceHandler<A, StateActionPair<A>> {

    private List<StateActionPair<A>> stateActionPairs = new ArrayList<>();

    public void setFinalObservation(Observation observation) {
        // Do nothing
    }

    public void addExperience(Observation observation, A action, double reward, boolean isTerminal) {
        stateActionPairs.add(new StateActionPair<A>(observation, action, reward, isTerminal));
    }

    @Override
    public int getTrainingBatchSize() {
        return stateActionPairs.size();
    }

    /**
     * The elements are returned in the historical order (i.e. in the order they happened)
     * Note: the experience store is cleared after calling this method.
     *
     * @return The list of experience elements
     */
    @Override
    public List<StateActionPair<A>> generateTrainingBatch() {
        List<StateActionPair<A>> result = stateActionPairs;
        stateActionPairs = new ArrayList<>();

        return result;
    }

    @Override
    public void reset() {
        stateActionPairs = new ArrayList<>();
    }

}
