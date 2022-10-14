package values;

//Contient les valeurs de texte invariables utilisé pour labelliser les informations affichées dans l'interface graphique
public final class MainFXTextConstants {
    //Valeurs affichées dans l'interface
    private static final String displayCallGraphButtonText = "Afficher le graphe d'appel";
    private static final String projectPathDirectoryChooserTitle = "Chemin de l'application";
    private static final String jrePathDirectoryChooserTitle = "Chemin du JRE";

    private MainFXTextConstants(){}
    private static final String graphDisplayTitle = "Graphe d'appel de l'application";
    private static final String numberOfClassDeclarationsUnchangingText = "Nombre de déclarations de classes : ";
    private static final String numberOfLinesInApplicationUnchangingText = "Nombre de lignes dans le projet : ";
    private static final String numberOfMethodsInApplicationUnchangingText = "Nombre de méthodes dans le projet : ";
    private static final String numberOfPackagesInApplicationUnchangingText = "Nombre de packages dans le projet : ";
    private static final String averageNumberOfMethodsPerClassUnchangingText = "Nombre moyen de méthodes par classe : ";
    private static final String averageNumberOfLinesOfCodesPerMethodUnchangingText = "Nombre moyen de ligne de codes par méthode : ";
    private static final String averageNumberOfFieldsPerClassUnchangingText = "Nombre moyen d'attributs par classe : ";
    private static final String top10PercentOfClassesWithMostMethodsUnchangingText = "10% de classes avec le plus de méthodes : ";
    private static final String top10PercentOfClassesWithMostFieldsUnchangingText = "10% de classes avec le plus d'attributs : ";
    private static final String top10PercentOfClassesWithMostMethodsAndFieldsUnchangingText = "10% de classes avec le plus de méthodes et d'attributs simultanément: ";
    private static final String classesWithMoreThanXMethodsUnchangingText1 = "Classes possédant plus de ";
    private static final String classesWithMoreThanXMethodsUnchangingText2 = " méthodes : ";
    private static final String top10PercentOfMethodsWithMostLinesOfCodesPerClassUnchangingText = "10% des méthodes possédant le plus de lignes (par classe): ";
    private static final String maximalNumberOfParametersInMethodsUnchangingText = "Plus grand nombre de paramètres dans une méthode : ";

    public static String getGraphDisplayTitle() {
        return graphDisplayTitle;
    }

    public static String getNumberOfClassDeclarationsUnchangingText() {
        return numberOfClassDeclarationsUnchangingText;
    }

    public static String getNumberOfLinesInApplicationUnchangingText() {
        return numberOfLinesInApplicationUnchangingText;
    }

    public static String getNumberOfMethodsInApplicationUnchangingText() {
        return numberOfMethodsInApplicationUnchangingText;
    }

    public static String getNumberOfPackagesInApplicationUnchangingText() {
        return numberOfPackagesInApplicationUnchangingText;
    }

    public static String getAverageNumberOfMethodsPerClassUnchangingText() {
        return averageNumberOfMethodsPerClassUnchangingText;
    }

    public static String getAverageNumberOfLinesOfCodesPerMethodUnchangingText() {
        return averageNumberOfLinesOfCodesPerMethodUnchangingText;
    }

    public static String getAverageNumberOfFieldsPerClassUnchangingText() {
        return averageNumberOfFieldsPerClassUnchangingText;
    }

    public static String getTop10PercentOfClassesWithMostMethodsUnchangingText() {
        return top10PercentOfClassesWithMostMethodsUnchangingText;
    }

    public static String getTop10PercentOfClassesWithMostFieldsUnchangingText() {
        return top10PercentOfClassesWithMostFieldsUnchangingText;
    }

    public static String getTop10PercentOfClassesWithMostMethodsAndFieldsUnchangingText() {
        return top10PercentOfClassesWithMostMethodsAndFieldsUnchangingText;
    }

    public static String getClassesWithMoreThanXMethodsUnchangingText1() {
        return classesWithMoreThanXMethodsUnchangingText1;
    }

    public static String getClassesWithMoreThanXMethodsUnchangingText2() {
        return classesWithMoreThanXMethodsUnchangingText2;
    }

    public static String getTop10PercentOfMethodsWithMostLinesOfCodesPerClassUnchangingText() {
        return top10PercentOfMethodsWithMostLinesOfCodesPerClassUnchangingText;
    }

    public static String getMaximalNumberOfParametersInMethodsUnchangingText() {
        return maximalNumberOfParametersInMethodsUnchangingText;
    }

    public static String getDisplayCallGraphButtonText() {
        return displayCallGraphButtonText;
    }

    public static String getProjectPathDirectoryChooserTitle() {
        return projectPathDirectoryChooserTitle;
    }

    public static String getJrePathDirectoryChooserTitle() {
        return jrePathDirectoryChooserTitle;
    }
}
