package values;

public final class MainFXConfig {
    //Pour la mise en forme
    private static final int sceneWidth = 1024;
    private static final int sceneHeight = 768;
    private static final int paneSpacing = 10;

    private MainFXConfig(){}

    public static int getSceneWidth() {
        return sceneWidth;
    }

    public static int getSceneHeight() {
        return sceneHeight;
    }

    public static int getPaneSpacing() {
        return paneSpacing;
    }
}
