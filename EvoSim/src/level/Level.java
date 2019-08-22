package level;

import java.util.ArrayList;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import box2d.B2DBody;
import javafx.scene.paint.Color;

public class Level {
    LevelStyle levelStyle;
    private float incline = 0.3f;
    private float hurdleDist = 2f;
    private float hurdleHeight = 0.2f;
    private float hurdleWidth = 0.2f;
    private float randDist = 2f;
    private float randMaxDiff = 2f;

    private static float thickness = 10f;
    private static float negX = 10f;
    private static float posX = 100f;

    private ArrayList<B2DBody> levelBodies;

    public Level() {
        //initIncline(0.2f);
        initRandom();
        //initFlat();
    }

    public void setLevelStyle(LevelStyle ls) {
        levelStyle = ls;
    }

    public void initFlat() {
        levelStyle = LevelStyle.FLAT;
        initStartArea();
        B2DBody f = new B2DBody("flat");
        f.setUpRect(posX/2f, -thickness/2f, posX/2f, thickness/2f, 0f, BodyType.STATIC);
        setVisuals(f);
        levelBodies.add(f);
    }
    public void initIncline(float incl_in) {
        levelStyle = LevelStyle.INCLINE;
        incline = incl_in;
        initStartArea();
        PolygonShape ps = new PolygonShape();
        Vec2[] verts = {
            new Vec2(0f, -thickness),
            new Vec2(posX, -thickness),
            new Vec2(posX, posX*incline),
            new Vec2(0f, 0f)
        };
        ps.set(verts, verts.length);
        B2DBody poly = new B2DBody("poly");
        poly.setPolygonShape(ps, new Vec2(posX/2f, thickness/2f));
        setVisuals(poly);
        poly.setBodyType(BodyType.STATIC);
        //poly.setPosition(new Vec2(posX/2f, 0));
        levelBodies.add(poly);
    }

    public float getIncline() {
        return incline;
    }


    public void initHurdles(float dist, float height, float width) {
        hurdleHeight = height;
        hurdleDist = dist;
        hurdleWidth = width;
        initHurdles();
    }

    public void initHurdles() {
        levelStyle = LevelStyle.HURDLES;
        initStartArea();
        float x = 0f;
        while (x < posX - hurdleWidth - hurdleDist) {
            x += hurdleDist;
            B2DBody h = new B2DBody("hurdle");
            h.setUpRect(x + hurdleWidth/2f, hurdleHeight/2f, hurdleWidth/2f, hurdleHeight/2f, 0, BodyType.STATIC);
            setVisuals(h);
            levelBodies.add(h);
        }        
        B2DBody f = new B2DBody("flat");
        f.setUpRect(posX/2f, -thickness/2f, posX/2f, thickness/2f, 0f, BodyType.STATIC);
        setVisuals(f);
        levelBodies.add(f);
    }

    public void initRandom() {
        levelStyle = LevelStyle.RANDOM;
        initStartArea();
        float x = 0f;
        float lastY = 0f;
        while (x < posX - randDist) {
            PolygonShape ps = new PolygonShape();
            float newY = randMaxDiff*(((float) Math.random()) - 0.5f);
            Vec2[] verts = {
                new Vec2(x, -thickness),
                new Vec2(x + randDist, -thickness),
                new Vec2(x + randDist, newY),
                new Vec2(x, lastY)
            };
            ps.set(verts, verts.length);
            B2DBody poly = new B2DBody("rand");
            poly.setPolygonShape(ps, new Vec2(randDist/2f, thickness/2f));
            poly.setBodyType(BodyType.STATIC);
            setVisuals(poly);
            levelBodies.add(poly);
            lastY = newY;
            x += randDist;
        }     
        
    }

    private void initStartArea() {
        levelBodies = new ArrayList<B2DBody>();
        levelBodies.add(new B2DBody("start"));
        levelBodies.get(0).setUpRect(-negX/2f, -thickness/2f, thickness/2f, negX/2f, 0f, BodyType.STATIC);
        setVisuals(levelBodies.get(0));
    }

    public ArrayList<B2DBody> getLevel() {
        ArrayList<B2DBody> out = new ArrayList<B2DBody>();
        for (B2DBody b : levelBodies) {
            out.add(b.clone());
        }
        return out;
    }

    private void setVisuals(B2DBody b) {
        b.setColor(Color.GREENYELLOW);
        b.setFill(true);
    }

}