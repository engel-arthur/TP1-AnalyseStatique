import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import values.MainFXConfig;
import values.MainFXTextConstants;
import values.Paths;

import java.io.*;


public class MainFX extends Application {

    //Valeur de X pour obtenir les classes ayant plus de X méthodes
    private int xValue = 5;

    private static VBox mainPane;
    private static Scene mainScene;

    private static Button displayCallGraphButton;

    private static HBox projectPathChooserPane;
    private static HBox jrePathChooserPane;
    private static Button projectPathChooserButton;
    private static Button jrePathChooserButton;

    private static DirectoryChooser projectPathChooser;
    private static DirectoryChooser jrePathChooser;

    //Label contenant les résultats
    private Label numberOfClassDeclarationsLabel;
    private Label numberOfLinesInApplicationLabel;
    private Label numberOfMethodsInProjectLabel;
    private Label numberOfPackagesInProjectLabel;
    private Label averageNumberOfMethodsPerClassLabel;
    private Label averageNumberOfLinesOfCodesPerMethodLabel;
    private Label averageNumberOfFieldsPerClassLabel;
    private Label top10PercentOfClassesWithMostMethodsLabel;
    private Label top10PercentOfClassesWithMostFieldsLabel;
    private Label top10PercentOfClassesWithMostMethodsAndFieldsLabel;
    private Label classesWithMoreThanXMethodsLabel;
    private Label top10PercentOfMethodsWithMostLinesOfCodesPerClassLabel;
    private Label maximalNumberOfParametersInMethodsLabel;

    private Label projectPathLabel;
    private Label jrePathLabel;

    //Valeurs variables affichée dans l'interface (chargées à l'aide de la classe Parser)
    private int numberOfClassDeclarations;
    private int numberOfLinesInApplication;
    private int numberOfMethodsInApplication;
    private int numberOfPackagesInApplication;
    private float averageNumberOfMethodsPerClass;
    private float averageNumberOfLinesOfCodesPerMethod;
    private float averageNumberOfFieldsPerClass;
    private String top10PercentOfClassesWithMostMethods = "";
    private String top10PercentOfClassesWithMostFields = "";
    private String top10PercentOfClassesWithMostMethodsAndFields = "";
    private String classesWithMoreThanXMethods = "";
    private String top10PercentOfMethodsWithMostLinesOfCodesPerClass = "";
    private int maximalNumberOfParametersInMethods;

    @Override
    public void start(Stage stage) throws IOException {
        //paramétrer les paths ici

        displayCallGraphButton = new Button(MainFXTextConstants.getDisplayCallGraphButtonText());
        setDisplayCallGraphButtonAction(displayCallGraphButton);


        projectPathChooser = new DirectoryChooser();
        projectPathChooser.setInitialDirectory(new File(Paths.getProjectPath()));

        projectPathChooserButton = new Button(MainFXTextConstants.getProjectPathDirectoryChooserTitle());
        setProjectPathChooserButtonAction(projectPathChooserButton, projectPathChooser, stage);


        jrePathChooser = new DirectoryChooser();
        jrePathChooser.setInitialDirectory(new File(Paths.getJrePath()));

        jrePathChooserButton = new Button(MainFXTextConstants.getJrePathDirectoryChooserTitle());
        setJrePathChooserButtonAction(jrePathChooserButton, jrePathChooser, stage);


        refreshInfo(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void refreshInfo(Stage stage) throws IOException {
        loadAllApplicationInfo();
        generateLabelsFromApplicationInfo();
        generatePanes();
        updateMainScene();
        stage.setScene(mainScene);
    }

    private void loadAllApplicationInfo() throws IOException {
        Parser.visitApplication();

        //On réinitialise les strings, pour le cas ou on rafraichit l'affichage (par exemple changement de chemin d'application)
        top10PercentOfClassesWithMostMethods = "";
        top10PercentOfClassesWithMostFields = "";
        top10PercentOfClassesWithMostMethodsAndFields = "";
        classesWithMoreThanXMethods = "";
        top10PercentOfMethodsWithMostLinesOfCodesPerClass = "";

        numberOfClassDeclarations = Parser.typeDeclarationVisitor.getClassDeclarationsCount();

        numberOfLinesInApplication = Parser.numberOfLinesInApplication;

        numberOfMethodsInApplication = Parser.methodDeclarationVisitor.getMethodDeclarationsCount();

        numberOfPackagesInApplication = Parser.packageDeclarationVisitor.getNumberOfPackages();

        averageNumberOfMethodsPerClass = Parser.typeDeclarationVisitor.getAverageNumberOfMethodsPerClass();

        averageNumberOfLinesOfCodesPerMethod = (float) Parser.numberOfLinesInMethodsOfApplication / Parser.methodDeclarationVisitor.getMethodDeclarationsCount();

        averageNumberOfFieldsPerClass = Parser.typeDeclarationVisitor.getAverageNumberOfFieldsPerClass();

        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getTop10PercentOfClassesWithMostMethods()) {
            top10PercentOfClassesWithMostMethods += typeDeclaration.getName() + " ";
        }

        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getTop10PercentOfClassesWithMostFields()) {
            top10PercentOfClassesWithMostFields += typeDeclaration.getName() + " ";
        }

        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getTop10PercentOfClassesWithMostMethodsAndFields()) {
            top10PercentOfClassesWithMostMethodsAndFields += typeDeclaration.getName() + " ";
        }
        if(top10PercentOfClassesWithMostMethodsAndFields.equals(""))
            top10PercentOfClassesWithMostMethodsAndFields = "Aucune";

        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getClassesWithMoreThanXMethods(xValue)) {
            classesWithMoreThanXMethods += typeDeclaration.getName() + " ";
        }
        if(classesWithMoreThanXMethods.equals(""))
            classesWithMoreThanXMethods = "Aucune";

        //TODO 10% de méthodes possédant le plus de lignes (par classe);
        top10PercentOfMethodsWithMostLinesOfCodesPerClass = "todo";

        maximalNumberOfParametersInMethods =  Parser.methodDeclarationVisitor.getMaximalNumberOfParametersInMethods();

    }

    //Génère tous les labels affichés par l'application
    //Les labels contenant les informations de l'application sont une concaténation d'une valeur correspondant à l'action souhaitée
    //et d'un texte constant décrivant cette valeur
    //Sont aussi générés des labels affichant les chemins projectPath et jrePath sélectionnés
    private void generateLabelsFromApplicationInfo() {
        numberOfClassDeclarationsLabel = new Label(MainFXTextConstants.getNumberOfClassDeclarationsUnchangingText() + numberOfClassDeclarations);
        numberOfLinesInApplicationLabel = new Label(MainFXTextConstants.getNumberOfLinesInApplicationUnchangingText() + numberOfLinesInApplication);
        numberOfMethodsInProjectLabel = new Label(MainFXTextConstants.getNumberOfMethodsInApplicationUnchangingText() + numberOfMethodsInApplication);
        numberOfPackagesInProjectLabel = new Label(MainFXTextConstants.getNumberOfPackagesInApplicationUnchangingText() + numberOfPackagesInApplication);
        averageNumberOfMethodsPerClassLabel = new Label(MainFXTextConstants.getAverageNumberOfMethodsPerClassUnchangingText() + averageNumberOfMethodsPerClass);
        averageNumberOfLinesOfCodesPerMethodLabel = new Label(MainFXTextConstants.getAverageNumberOfLinesOfCodesPerMethodUnchangingText() + averageNumberOfLinesOfCodesPerMethod);
        averageNumberOfFieldsPerClassLabel = new Label(MainFXTextConstants.getAverageNumberOfFieldsPerClassUnchangingText() + averageNumberOfFieldsPerClass);
        top10PercentOfClassesWithMostMethodsLabel = new Label(MainFXTextConstants.getTop10PercentOfClassesWithMostMethodsUnchangingText() + top10PercentOfClassesWithMostMethods);
        top10PercentOfClassesWithMostFieldsLabel = new Label(MainFXTextConstants.getTop10PercentOfClassesWithMostFieldsUnchangingText() + top10PercentOfClassesWithMostFields);
        top10PercentOfClassesWithMostMethodsAndFieldsLabel = new Label(MainFXTextConstants.getTop10PercentOfClassesWithMostMethodsAndFieldsUnchangingText() + top10PercentOfClassesWithMostMethodsAndFields);
        classesWithMoreThanXMethodsLabel = new Label(MainFXTextConstants.getClassesWithMoreThanXMethodsUnchangingText1() + xValue + MainFXTextConstants.getClassesWithMoreThanXMethodsUnchangingText2() + classesWithMoreThanXMethods);
        top10PercentOfMethodsWithMostLinesOfCodesPerClassLabel = new Label(MainFXTextConstants.getTop10PercentOfMethodsWithMostLinesOfCodesPerClassUnchangingText() + top10PercentOfMethodsWithMostLinesOfCodesPerClass);
        maximalNumberOfParametersInMethodsLabel = new Label(MainFXTextConstants.getMaximalNumberOfParametersInMethodsUnchangingText() + maximalNumberOfParametersInMethods);
        projectPathLabel = new Label(values.Paths.getProjectPath());
        jrePathLabel = new Label(values.Paths.getJrePath());
    }

    //Génère les panneaux affichés par l'interface
    private void generatePanes() {
        projectPathChooserPane = new HBox(projectPathChooserButton, projectPathLabel);
        jrePathChooserPane = new HBox(jrePathChooserButton, jrePathLabel);

        projectPathChooserPane.setAlignment(Pos.valueOf("CENTER"));
        projectPathChooserPane.setSpacing(MainFXConfig.getPaneSpacing());

        jrePathChooserPane.setAlignment(Pos.valueOf("CENTER"));
        jrePathChooserPane.setSpacing(MainFXConfig.getPaneSpacing());

        //Sont passés tous les labels et boutons
        mainPane = new VBox(
                numberOfClassDeclarationsLabel,
                numberOfLinesInApplicationLabel,
                numberOfMethodsInProjectLabel,
                numberOfPackagesInProjectLabel,
                averageNumberOfMethodsPerClassLabel,
                averageNumberOfLinesOfCodesPerMethodLabel,
                averageNumberOfFieldsPerClassLabel,
                top10PercentOfClassesWithMostMethodsLabel,
                top10PercentOfClassesWithMostFieldsLabel,
                top10PercentOfClassesWithMostMethodsAndFieldsLabel,
                classesWithMoreThanXMethodsLabel,
                top10PercentOfMethodsWithMostLinesOfCodesPerClassLabel,
                maximalNumberOfParametersInMethodsLabel,
                displayCallGraphButton,
                projectPathChooserPane,
                jrePathChooserPane
        );

        //On centre et on espace les éléments pour rentre l'interface plus jolie
        mainPane.setAlignment(Pos.valueOf("CENTER"));
        mainPane.setSpacing(MainFXConfig.getPaneSpacing());
    }

    //Appelé quand on rafraichit, permet de charger de nouvelles données
    private void updateMainScene() {
        mainScene = new Scene(mainPane, MainFXConfig.getSceneWidth(), MainFXConfig.getSceneHeight());
    }

    //On définit l'action effectuée sur clic du bouton permettant d'afficher le graphe d'appel
    private void setDisplayCallGraphButtonAction(Button displayCallGraphButton) {
        displayCallGraphButton.setOnAction(actionEvent -> {
            //On sérialise le graphe
            String serializedCallGraph = utils.GraphUtils.serializeGraphToDotFormat(Parser.callGraph);
            Image graphImage;

            //On essaye de le sauvegarder sous format png au chemin contenu dans la classe Paths
            try {
                utils.GraphUtils.convertGraphSerializedInDotFormatToPNG(serializedCallGraph, Paths.getPathToSaveCallGraphPNGTo());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //On essaie de récupérer l'image sauvegardée pour la charger dans l'interface
            try {
                graphImage = getImageFromPath(Paths.getPathToSaveCallGraphPNGTo());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            //On génère une imageview qui permettra d'afficher l'Image
            ImageView graphImageView = generateImageView(graphImage);

            //On le met dans un panneau scrollable car l'image sera souvent assez large
            ScrollPane graphImagePane = new ScrollPane(graphImageView);

            //On crée une scène contenant l'image, qui sera chargée dans une nouvelle fenêtre, ensuite affichée
            Scene graphDisplayScene = new Scene(graphImagePane, MainFXConfig.getSceneWidth(), MainFXConfig.getSceneHeight());
            Stage graphDisplayWindow = new Stage();

            graphDisplayWindow.setTitle(MainFXTextConstants.getGraphDisplayTitle());
            graphDisplayWindow.setScene(graphDisplayScene);

            graphDisplayWindow.show();
        });
    }

    private Image getImageFromPath(String path) throws FileNotFoundException {
        InputStream stream = new FileInputStream(path);
        return new Image(stream);
    }

    private ImageView generateImageView(Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private void setProjectPathChooserButtonAction(Button projectPathChooserButton, DirectoryChooser projectPathDirectoryChooser, Stage stage) {
        projectPathChooserButton.setOnAction(actionEvent -> {
            File newFolder = projectPathDirectoryChooser.showDialog(stage);
            if(newFolder!=null) {
                values.Paths.setProjectPath(newFolder.toString());
                try {
                    refreshInfo(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void setJrePathChooserButtonAction(Button JrePathChooserButton, DirectoryChooser JrePathDirectoryChooser, Stage stage) {
        JrePathChooserButton.setOnAction(actionEvent -> {
            File newFolder = JrePathDirectoryChooser.showDialog(stage);
            if(newFolder!=null) {
                values.Paths.setJrePath(newFolder.toString());
                try {
                    refreshInfo(stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
