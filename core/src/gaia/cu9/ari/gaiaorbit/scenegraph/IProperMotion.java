/*
 * This file is part of Gaia Sky, which is released under the Mozilla Public License 2.0.
 * See the file LICENSE.md in the project root for full license details.
 */

package gaia.cu9.ari.gaiaorbit.scenegraph;

/**
 * Any entity which contains a proper motion
 *
 * @author tsagrista
 */
public interface IProperMotion {

    /**
     * Returns the mu alpha in mas/yr
     *
     * @return The mu alpha in mas/yr
     */
    double getMuAlpha();

    /**
     * Returns the mu delta in mas/yr
     *
     * @return The mu delta in mas/yr
     */
    double getMuDelta();

    /**
     * Returns the radial velocity in km/s
     *
     * @return The radial velocity in km/s
     */
    double getRadialVelocity();

}
