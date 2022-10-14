import org.eclipse.jdt.core.dom.TypeDeclaration;
import values.Paths;

import java.io.IOException;

public class MainNoInterface {
    //Un main primitif permettant de tester rapidement la classe Parser
    public static void main(String[] args) throws IOException {// read java files
        Parser.visitApplication();

        //Affichage partie 2 TP1
        System.out.println("Nombre de classes dans l'application : " + Parser.typeDeclarationVisitor.getClassDeclarationsCount() + "\n");

        System.out.println("Nombre de lignes dans le projet : " + Parser.numberOfLinesInApplication + "\n");

        System.out.println("Nombre de méthodes dans le projet : " + Parser.methodDeclarationVisitor.getMethodDeclarationsCount() + "\n");

        System.out.println("Nombre de packages dans le projet : " + Parser.packageDeclarationVisitor.getNumberOfPackages() + "\n");

        System.out.println("Nombre de méthodes moyen par classe : " + Parser.typeDeclarationVisitor.getAverageNumberOfMethodsPerClass() + "\n");

        System.out.println("Nombre moyen de ligne de codes par méthode : " + (float) Parser.numberOfLinesInMethodsOfApplication / Parser.methodDeclarationVisitor.getMethodDeclarationsCount() + "\n");

        System.out.println("Nombre moyen d'attributs par classe : " + Parser.typeDeclarationVisitor.getAverageNumberOfFieldsPerClass() + "\n");

        System.out.println("10% de classes possédant le plus grand nombre de méthodes :");
        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getTop10PercentOfClassesWithMostMethods()) {
            System.out.print(typeDeclaration.getName() + " ");
        }
        System.out.println("\n");

        System.out.println("10% de classes possédant le plus grand nombre d'attributs :");
        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getTop10PercentOfClassesWithMostFields()) {
            System.out.print(typeDeclaration.getName() + " ");
        }
        System.out.println("\n");

        System.out.println("Classes étant à la fois parmi les 10% ayant le plus grand nombre d'attributs ET de méthodes : ");
        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getTop10PercentOfClassesWithMostMethodsAndFields()) {
            System.out.print(typeDeclaration.getName() + " ");
        }
        System.out.println("\n");

        System.out.println("Classes possédant plus de 5 méthodes : ");
        for(TypeDeclaration typeDeclaration : Parser.typeDeclarationVisitor.getClassesWithMoreThanXMethods(5)) {
            System.out.print(typeDeclaration.getName() + " ");
        }
        System.out.println("\n");

        System.out.println("10% de méthodes possédant le plus de lignes (par classe)\n");

        System.out.println("Nombre maximal de paramètres par rapport à toutes les méthodes de l'application : " + Parser.methodDeclarationVisitor.getMaximalNumberOfParametersInMethods() + "\n");

        System.out.println("Graphe d'appel du code analysé sauvegardé dans ./" + Paths.getPathToSaveCallGraphPNGTo());
        String serializedCallGraph = utils.GraphUtils.serializeGraphToDotFormat(Parser.callGraph);
        utils.GraphUtils.convertGraphSerializedInDotFormatToPNG(serializedCallGraph, Paths.getPathToSaveCallGraphPNGTo());
    }
}
