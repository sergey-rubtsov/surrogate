/*******************************************************************************
 * Copyright (c) 2015-2019 Skymind, Inc.
 * Copyright (c) 2020 Konduit K.K.
 *
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

package org.deeplearning4j.rl4j.learning.async;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.experience.ExperienceHandler;
import org.deeplearning4j.rl4j.experience.StateActionExperienceHandler;
import org.deeplearning4j.rl4j.learning.IHistoryProcessor;
import org.deeplearning4j.rl4j.learning.async.a3c.discrete.AdvantageActorCriticUpdateAlgorithm;
import org.deeplearning4j.rl4j.learning.configuration.LearningConfiguration;
import org.deeplearning4j.rl4j.learning.listener.TrainingListenerList;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.NeuralNet;
import org.deeplearning4j.rl4j.network.ac.IActorCritic;
import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.policy.ACPolicy;
import org.deeplearning4j.rl4j.policy.IPolicy;
import org.deeplearning4j.rl4j.policy.Policy;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.rng.Random;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author rubenfiszel (ruben.fiszel@epfl.ch) on 8/5/16.
 * <p>
 * Async Learning specialized for the Discrete Domain
 */
public class AsyncThreadDiscrete<OBSERVATION extends Encodable, NN extends NeuralNet>
        extends AsyncThread<OBSERVATION, Integer, DiscreteSpace, NN> {

    @Getter
    final protected LearningConfiguration configuration;
    @Getter
    final protected IAsyncGlobal<NN> asyncGlobal;

    @Getter
    private final NN current;

    final private Random rnd;

    @Setter(AccessLevel.PROTECTED)
    private UpdateAlgorithm<NN> updateAlgorithm;

    // TODO: Make it configurable with a builder
    @Setter(AccessLevel.PROTECTED)
    @Getter
    private ExperienceHandler experienceHandler = new StateActionExperienceHandler();

    public AsyncThreadDiscrete(
                               MDP<OBSERVATION, Integer, DiscreteSpace> mdp,
                               IAsyncGlobal<NN> asyncGlobal,
                               LearningConfiguration configuration,
                               TrainingListenerList listeners,
                               int threadNumber,
                               int deviceNum) {
        super(mdp, listeners, threadNumber, deviceNum);
        this.asyncGlobal = asyncGlobal;
        this.configuration = configuration;
        synchronized (asyncGlobal) {
            current = (NN) asyncGlobal.getTarget().clone();
        }
        Long seed = configuration.getSeed();
        rnd = Nd4j.getRandom();
        if (seed != null) {
            rnd.setSeed(seed + threadNumber);
        }

        setUpdateAlgorithm(buildUpdateAlgorithm());
    }

    protected Policy<Integer> getPolicy(IActorCritic net) {
        return new ACPolicy(net, rnd);
    }

    /**
     * calc the gradients based on the n-step rewards
     */

    protected UpdateAlgorithm<NN> buildUpdateAlgorithm() {
        int[] shape = getHistoryProcessor() == null ? getMdp().getObservationSpace().getShape() : getHistoryProcessor().getConf().getShape();
        return (UpdateAlgorithm<NN>) new AdvantageActorCriticUpdateAlgorithm(asyncGlobal.getTarget().isRecurrent(), shape, getMdp().getActionSpace().getSize(), configuration.getGamma());
    }

    @Override
    public void setHistoryProcessor(IHistoryProcessor historyProcessor) {
        super.setHistoryProcessor(historyProcessor);
        updateAlgorithm = buildUpdateAlgorithm();
    }

    @Override
    protected void preEpisode() {
        experienceHandler.reset();
    }


    /**
     * "Subepoch"  correspond to the t_max-step iterations
     * that stack rewards with t_max MiniTrans
     *
     * @param sObs          the obs to start from
     * @param trainingSteps the number of training steps
     * @return subepoch training informations
     */
    public SubEpochReturn trainSubEpoch(Observation sObs, int trainingSteps) {

        current.copy(getAsyncGlobal().getTarget());

        Observation obs = sObs;
        IPolicy<Integer> policy = getPolicy((IActorCritic) current);

        Integer action = getMdp().getActionSpace().noOp();

        double reward = 0;
        double accuReward = 0;

        while (!getMdp().isDone() && experienceHandler.getTrainingBatchSize() != trainingSteps) {

            //if step of training, just repeat lastAction
            if (!obs.isSkipped()) {
                action = policy.nextAction(obs);
            }

            StepReply<Observation> stepReply = getLegacyMDPWrapper().step(action);
            accuReward += stepReply.getReward() * getConfiguration().getRewardFactor();

            if (!obs.isSkipped()) {
                experienceHandler.addExperience(obs, action, accuReward, stepReply.isDone());
                accuReward = 0;

                incrementSteps();
            }

            obs = stepReply.getObservation();
            reward += stepReply.getReward();

        }

        boolean episodeComplete = getMdp().isDone() || getConfiguration().getMaxEpochStep() == currentEpisodeStepCount;

        if (episodeComplete && experienceHandler.getTrainingBatchSize() != trainingSteps) {
            experienceHandler.setFinalObservation(obs);
        }

        int experienceSize = experienceHandler.getTrainingBatchSize();

        getAsyncGlobal().applyGradient(updateAlgorithm.computeGradients(current, experienceHandler.generateTrainingBatch()), experienceSize);

        return new SubEpochReturn(experienceSize, obs, reward, current.getLatestScore(), episodeComplete);
    }

}
