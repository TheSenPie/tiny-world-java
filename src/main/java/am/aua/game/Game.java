package am.aua.game;

import am.aua.rendererEngine.Window;

public class Game implements ApplicationListener{

    public static void main(String[] args) {
        Game game = new Game();
        Window.create(game);
        Window.loop();
    }

    @Override
    public void create() {
//        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//        });
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

    @Override
    public void resize() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
