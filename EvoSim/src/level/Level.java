package level;

import java.io.Serializable;
import java.util.ArrayList;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import box2d.B2DBody;
import javafx.scene.paint.Color;

public class Level implements Serializable {
	private static final long serialVersionUID = 1L;
    LevelStyle levelStyle;
    private LevelSettings levelSettings = new LevelSettings();

    private static float climbHeight = 50f;
    private static float thickness = 20f;
    private static float negX = 20f;
    private static float posX = 100f;

    public Level() {
        levelStyle = LevelStyle.FLAT;
    }

    public void getFlat(ArrayList<B2DBody> levelBodies) {
        levelStyle = LevelStyle.FLAT;
        getStartArea(levelBodies);
        B2DBody f = new B2DBody("flat");
        f.setUpRect(posX/2f, -thickness/2f, posX/2f, thickness/2f, 0f, BodyType.STATIC);
        setVisuals(f);
        levelBodies.add(f);
    }

    public void getIncline(ArrayList<B2DBody> levelBodies) {
        levelStyle = LevelStyle.INCLINE;
        getStartArea(levelBodies);
        PolygonShape ps = new PolygonShape();
        Vec2[] verts = {
            new Vec2(0f, -thickness),
            new Vec2(posX, -thickness),
            new Vec2(posX, posX*levelSettings.incline),
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

    public void getHurdles(ArrayList<B2DBody> levelBodies) {
        levelStyle = LevelStyle.HURDLES;
        getStartArea(levelBodies);
        float x = 0f;
        while (x < posX - levelSettings.hurdleWidth - levelSettings.hurdleDist) {
            x += levelSettings.hurdleDist;
            B2DBody h = new B2DBody("hurdle");
            h.setUpRect(x + levelSettings.hurdleWidth/2f, levelSettings.hurdleHeight/2f,
                levelSettings.hurdleWidth/2f, levelSettings.hurdleHeight/2f, 0, BodyType.STATIC);
            setVisuals(h);
            levelBodies.add(h);
        }        
        B2DBody f = new B2DBody("flat");
        f.setUpRect(posX/2f, -thickness/2f, posX/2f, thickness/2f, 0f, BodyType.STATIC);
        setVisuals(f);
        levelBodies.add(f);
    }

    public void getClimb(ArrayList<B2DBody> levelBodies) {
        levelStyle = LevelStyle.CLIMB;
        getStartArea(levelBodies);
        B2DBody w1 = new B2DBody("wall"), w2 = new B2DBody("wall");
        w1.setUpRect(-levelSettings.climbWidth - 10f, climbHeight/2f, 10f, climbHeight/2f, 0, BodyType.STATIC);
        w2.setUpRect(10f, climbHeight/2f, 10f, climbHeight/2f, 0, BodyType.STATIC);
        setVisuals(w1);
        setVisuals(w2);
        levelBodies.add(w1);
        levelBodies.add(w2);     
        B2DBody f = new B2DBody("flat");
        f.setUpRect(negX/2f, -thickness/2f, negX/2f, thickness/2f, 0f, BodyType.STATIC);
        setVisuals(f);
        levelBodies.add(f);
    }

    public void getJump(ArrayList<B2DBody> levelBodies) {
        levelStyle = LevelStyle.JUMP;
        getStartArea(levelBodies);  
        B2DBody f = new B2DBody("flat");
        f.setUpRect(negX/2f, -thickness/2f, negX/2f, thickness/2f, 0f, BodyType.STATIC);
        setVisuals(f);
        levelBodies.add(f);
    }
/*
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
        
    }*/

    private void getStartArea(ArrayList<B2DBody> levelBodies) {
        levelBodies.add(new B2DBody("start"));
        levelBodies.get(0).setUpRect(-negX/2f, -thickness/2f, thickness/2f, negX/2f, 0f, BodyType.STATIC);
        setVisuals(levelBodies.get(0));
    }

    public ArrayList<B2DBody> getLevel() {
        ArrayList<B2DBody> levelBodies = new ArrayList<B2DBody>();
        switch (levelStyle) {
            case CLIMB:
                getClimb(levelBodies);
                break;
            case FLAT:
                getFlat(levelBodies);
                break;
            case JUMP:
                getJump(levelBodies);
                break;
            case RANDOM:
                
                break;
            case HURDLES:
                getHurdles(levelBodies);
                break;
            case INCLINE:
                getIncline(levelBodies);
                break;
        
            default:
                break;
        }
        return levelBodies;
    }

    private void setVisuals(B2DBody b) {
        b.setColor(Color.GREENYELLOW);
        b.setFill(true);
    }

    public LevelStyle getLevelStyle() {
        return levelStyle;
    }

    public void setLevelStyle(LevelStyle ls_in) {
        levelStyle = ls_in;
    }

	public boolean isVertical() {
        if (levelStyle == LevelStyle.CLIMB || levelStyle == LevelStyle.JUMP) return true;
		return false;
    }
    
    public LevelSettings getLevelSettings() {
        return levelSettings;
    }
}