package am.aua.game;

public interface ApplicationListener {
    void create();
    void dispose();
    void update();
    void render();
    void resize();

    // not implemented yet
    void pause();
    void resume();
}
