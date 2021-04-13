import com.jme3.bullet.animation.CenterHeuristic;
import com.jme3.bullet.animation.DynamicAnimControl;
import com.jme3.bullet.animation.LinkConfig;
import com.jme3.bullet.animation.MassHeuristic;
import com.jme3.bullet.animation.RangeOfMotion;
import com.jme3.bullet.animation.ShapeHeuristic;
import com.jme3.math.Vector3f;

public class WControl extends DynamicAnimControl {

    public WControl() {
        super();
        LinkConfig config1 = new LinkConfig(1f, MassHeuristic.Density,
                ShapeHeuristic.VertexHull, new Vector3f(1f, 1f, 1f),
                CenterHeuristic.Mean);
        LinkConfig config2 = new LinkConfig(1f, MassHeuristic.Density,
                ShapeHeuristic.TwoSphere, new Vector3f(1f, 1f, 1f),
                CenterHeuristic.Mean);
        super.setConfig("", config1);
        super.link("forearm.1.L", config1,
                new RangeOfMotion(0.06f, -2.4f, 1.26f, -0.29f, 1.94f, -0.74f));
        super.link("chest", config1,
                new RangeOfMotion(0.2f, 0f, 0.01f, -0.3f, 0.21f, -0.21f));
        super.link("upper_arm.1.R", config2,
                new RangeOfMotion(0.79f, -1.22f, 1.31f, -0.03f, 1.31f, -0.03f));
        super.link("upper_arm.1.L", config2,
                new RangeOfMotion(1.05f, -0.47f, 1.05f, 0f, 0f, -1.54f));
        super.link("shoulder.R", config1,
                new RangeOfMotion(0.14f, -0.24f, 0.07f, -0.01f, 0f, 0f));
        super.link("forearm.1.R", config1,
                new RangeOfMotion(2.34f, -0.19f, 1.28f, -0.2f, 2.05f, -1.86f));
        super.link("spine", config1,
                new RangeOfMotion(0f, -0.38f, 0.69f, -0.04f, 0.16f, 0f));
        super.link("foot.R", config1,
                new RangeOfMotion(1.03f, -0.28f, 0.35f, -0.3f, 0.17f, -0.17f));
        super.link("neck", config1,
                new RangeOfMotion(0.21f, -0.04f, 0f, -0.11f, 0.03f, -0.25f));
        super.link("hand.L", config1,
                new RangeOfMotion(0.38f, -0.22f, 0.44f, -0.44f, 0.6f, -0.36f));
        super.link("head", config1,
                new RangeOfMotion(0.12f, -0.4f, 0.04f, -0.19f, 0.23f, -0.81f));
        super.link("shin.L", config1,
                new RangeOfMotion(0.1f, -2.29f, 0f, 0f, 0f, 0f));
        super.link("upper_chest", config1,
                new RangeOfMotion(0.09f, 0f, 0.03f, -0.03f, 0.12f, -0.13f));
        super.link("hand.R", config1,
                new RangeOfMotion(0.45f, -0.23f, 0.45f, -0.65f, 0.39f, -2.74f));
        super.link("foot.L", config1,
                new RangeOfMotion(0.47f, -0.35f, 0.19f, -0.22f, 0.02f, -0.02f));
        super.link("thigh.L", config1,
                new RangeOfMotion(1.74f, -0.65f, 0.16f, -0.07f, 0.12f, -1.33f));
        super.link("shin.R", config1,
                new RangeOfMotion(0.1f, -2.28f, 0f, 0f, 0f, 0f));
        super.link("thigh.R", config1,
                new RangeOfMotion(3.11f, -3.02f, 0.05f, -1.33f, 2.97f, -0.18f));
        super.link("toe.R", config1,
                new RangeOfMotion(0.08f, -0.89f, 0.06f, -0.06f, 0.01f, -0.02f));
        super.link("shoulder.L", config1,
                new RangeOfMotion(0.14f, 0f, 0.16f, 0f, 0f, 0f));
        super.link("toe.L", config1,
                new RangeOfMotion(0.08f, -0.94f, 0.16f, -0.04f, 0.03f, -0.01f));
    }
}
