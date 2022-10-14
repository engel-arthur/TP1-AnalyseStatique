package values;

public final class Paths {
    private Paths(){}
    //public static final String projectPath = "/home/e20160001520/Documents/m2gl/HAI913 - Évolution et restructuration des logiciels/TP1-AnalyseStatique/";
    private static String projectPath = "C:\\Users\\artie\\Documents\\Cours\\m2gl\\HAI913 - Évolution et restructuration des logiciels\\TP1-AnalyseStatique";
    private static String projectSourcePath = projectPath + "/src";
    //public static final String jrePath = "/home/e20160001520/.jdks/corretto-18.0.2/lib/jrt-fs.jar";
    private static String jrePath = "C:\\Users\\artie\\.jdks\\openjdk-19\\lib\\jrt-fs.jar";
    private static String pathToSaveCallGraphPNGTo = "resources/graphRenders/callGraph.png";

    public static String getProjectPath() {
        return projectPath;
    }

    public static String getProjectSourcePath() {
        return projectSourcePath;
    }

    public static String getJrePath() {
        return jrePath;
    }

    public static String getPathToSaveCallGraphPNGTo() {
        return pathToSaveCallGraphPNGTo;
    }

    public static void setProjectPath(String path) {
        projectPath = path;
        projectSourcePath = projectPath + "/src";
    }

    public static void setJrePath(String path) {
        jrePath = path;
    }
}
