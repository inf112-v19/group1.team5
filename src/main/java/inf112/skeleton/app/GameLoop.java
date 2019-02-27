package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Vector;

public class GameLoop extends ApplicationAdapter implements InputProcessor {
    private static int[][] robot_start_positions = {
            {6, 5},
            {6, 6},
            {6, 7},
            {6, 8}
    };
    TiledMap tiledMap;
    Vector2D map_dim;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    // All positions are in board dimensions, not in pixel dimensions.
    Vector<IRenderable> board_render_queue;
    private Music player;
    private SpriteBatch batch;
    private Robot my_robot;
    private Game game;

    int start_pw, start_ph;

    @Override
    public void create () {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("./resources/map.tmx");
        System.out.println(tiledMap.getProperties());
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        batch = new SpriteBatch();
        board_render_queue = new Vector<>();
        map_dim = new Vector2D(
                tiledMap.getProperties().get("width", Integer.class),
                tiledMap.getProperties().get("height", Integer.class));
        FileHandle file = new FileHandle("resources/RoboLazer.mp3");
        player = Gdx.audio.newMusic(file);
        player.setLooping(true);

        ArrayList<Robot> robots = new ArrayList<>();
        for (int[] pos : robot_start_positions) {
            Robot robut = new Robot(pos[0], pos[1]);
            robots.add(robut);
            board_render_queue.add(robut);
        }
        my_robot = robots.get(robots.size() - 1);
        my_robot.rot(-90);

        System.out.println("Map Dimensions: " + map_dim);

        this.game = new Game(map_dim.getX(), map_dim.getY(), robots);

        Gdx.input.setInputProcessor(this);

        start_pw = w;
        start_ph = h;
    }

    public Vector2D toPixelCoordinate(Vector2D vec) {
        int pw = start_pw;
        int ph = start_ph;
        int w = map_dim.getX();
        int h = map_dim.getY();
        return new Vector2D(vec.getX() * (pw / w), vec.getY() * (ph / h));
    }

    public void render () {
        tiledMapRenderer.setView(camera);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        tiledMapRenderer.render();
        batch.begin();
        for (IRenderable r : board_render_queue) {
            Vector2D pos = r.getPos();
            Vector2D px_pos = toPixelCoordinate(pos);
            batch.draw(r.getTexture(), px_pos.getX(), px_pos.getY());
        }
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        int dir = 1;
        switch (keycode) {
            case Input.Keys.DOWN:
                dir = -1;
            case Input.Keys.UP:
                Vector2D dir_v = my_robot.getDir().copy();
                dir_v.mul(dir);
                if (game.canMoveTo(my_robot.getPos(), dir_v, my_robot)) {
                    my_robot.move(dir);
                }
                System.out.println(my_robot.getPos());
                TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0); // assuming the layer at index on contains tiles
                TiledMapTileLayer.Cell cell = layer.getCell(my_robot.getPos().getX(), my_robot.getPos().getY());
                System.out.println(cell.getTile().getProperties().get("MapObject", String.class));
                break;
            case Input.Keys.RIGHT:
                my_robot.rot(-90);
                break;
            case Input.Keys.LEFT:
                my_robot.rot(90);
                break;
            case Input.Keys.M:
                if (!player.isPlaying())
                    player.play();
                else
                    player.stop();
                break;
            case Input.Keys.F:
                game.printFlags();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    public void addBoardDrawJob(IRenderable obj) {
        board_render_queue.add(obj);
    }

}