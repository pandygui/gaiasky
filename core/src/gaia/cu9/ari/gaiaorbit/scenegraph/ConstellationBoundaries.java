/*
 * This file is part of Gaia Sky, which is released under the Mozilla Public License 2.0.
 * See the file LICENSE.md in the project root for full license details.
 */

package gaia.cu9.ari.gaiaorbit.scenegraph;

import com.badlogic.gdx.graphics.GL20;
import gaia.cu9.ari.gaiaorbit.render.ILineRenderable;
import gaia.cu9.ari.gaiaorbit.render.system.LineRenderSystem;
import gaia.cu9.ari.gaiaorbit.scenegraph.camera.ICamera;
import gaia.cu9.ari.gaiaorbit.util.math.Vector3d;
import gaia.cu9.ari.gaiaorbit.util.time.ITimeFrameProvider;

import java.util.List;

public class ConstellationBoundaries extends AbstractPositionEntity implements ILineRenderable {
    private float alpha = .8f;
    private List<List<Vector3d>> boundaries;

    public ConstellationBoundaries() {
        super();
        cc = new float[] { .8f, .8f, 1f, alpha };
        this.name = "Constellation boundaries";
        this.parentName = SceneGraphNode.ROOT_NAME;
    }

    @Override
    public void render(LineRenderSystem renderer, ICamera camera, float alpha) {
        alpha *= this.alpha;
        // This is so that the shape renderer does not mess up the z-buffer
        for (List<Vector3d> points : boundaries) {

            Vector3d previous = null;
            for (Vector3d point : points) {
                if (previous != null) {
                    renderer.addLine(this, (float) previous.x, (float) previous.y, (float) previous.z, (float) point.x, (float) point.y, (float) point.z, cc[0], cc[1], cc[2], alpha);
                }
                previous = point;
            }

        }
    }

    public void setBoundaries(List<List<Vector3d>> boundaries) {
        this.boundaries = boundaries;
    }

    @Override
    protected void addToRenderLists(ICamera camera) {
        // Add to toRender list
        addToRender(this, RenderGroup.LINE);
    }

    @Override
    public void updateLocalValues(ITimeFrameProvider time, ICamera camera) {
    }

    @Override
    public void initialize() {
    }

    @Override
    public float getLineWidth() {
        return 1;
    }

    @Override
    public int getGlType() {
        return GL20.GL_LINES;
    }
}
