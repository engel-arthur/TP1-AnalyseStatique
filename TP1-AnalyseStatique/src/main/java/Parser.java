import astvisitors.*;
import utils.Pair;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import values.Paths;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Cette classe permet d'analyser statiquement une application située à un chemin donné.
// Elle va pour cela parcourir tous les fichiers de l'application, et les parser pour en faire des CompilationUnit.
// Ces CompilationUnit accepteront des ASTVisitor qui réuniront des informations sur l'application.
// Diverses méthodes sont fournies dans les classes de ces visiteurs ou la classe ci-présente afin de récupérer ces
// informations après analyse de l'application.
// Pour fonctionner, cette classe à besoin d'un projectPath (chemin de l'application à analyser) et d'un jrePath (chemin menant au
// jdk) valides.

public class Parser {

	public static int numberOfLinesInApplication;
	public static int numberOfLinesInMethodsOfApplication ;
	public static TypeDeclarationVisitor typeDeclarationVisitor;
	public static MethodDeclarationVisitor methodDeclarationVisitor;
	public static PackageDeclarationVisitor packageDeclarationVisitor;
	public static Graph<String, DefaultEdge> callGraph;

	//Fonction principale, initialise les visiteurs et compteurs de lignes, et parse tous les fichiers de l'application,
	//en acceptant les différents visiteurs pour chaque fichier. Elle génère ensuite le graphe d'appel de l'application.
	public static void visitApplication() throws IOException {
		//Initialisation
		final File projectSourceFolder = new File(Paths.getProjectSourcePath());
		ArrayList<File> javaFilesInApplication = listJavaFilesForFolder(projectSourceFolder);
		numberOfLinesInApplication = 0;
		numberOfLinesInMethodsOfApplication = 0;
		typeDeclarationVisitor = new TypeDeclarationVisitor();
		methodDeclarationVisitor = new MethodDeclarationVisitor();
		packageDeclarationVisitor = new PackageDeclarationVisitor();

		//Parcours de l'application
		for (File fileEntry : javaFilesInApplication) {
			//On convertit le contenu du fichier en charArray pour pouvoir le passer à la fonction parse
			//de compilationunit
			String fileContentAsString = FileUtils.readFileToString(fileEntry, "UTF-8");
			CompilationUnit parsedFile = parse(fileContentAsString.toCharArray());
			//printMethodInfo(parsedFile);
			//printVariableInfo(parsedFile);
			//printMethodInvocationInfo(parsedFile);

			numberOfLinesInApplication += getNumberOfLinesInCompilationUnit(parsedFile);
			numberOfLinesInMethodsOfApplication += getTotalNumberOfLinesInMethodsOfACompilationUnit(parsedFile);
			parsedFile.accept(typeDeclarationVisitor);
			parsedFile.accept(methodDeclarationVisitor);
			parsedFile.accept(packageDeclarationVisitor);
		}

		callGraph = buildCallGraph();
	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<>();
		for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS18); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = {Paths.getProjectSourcePath()};
		String[] classpath = {Paths.getJrePath()};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	// navigate method information
	public static void printMethodInfo(CompilationUnit parse) {
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethodDeclarations()) {
			System.out.println("Method name: " + method.getName()
					+ " Return type: " + method.getReturnType2());
		}

	}

	// navigate variables inside method
	public static void printVariableInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethodDeclarations()) {

			VariableDeclarationFragmentVisitor visitor2 = new VariableDeclarationFragmentVisitor();
			method.accept(visitor2);

			for (VariableDeclarationFragment variableDeclarationFragment : visitor2
					.getVariables()) {
				System.out.println("variable name: "
						+ variableDeclarationFragment.getName()
						+ " variable Initializer: "
						+ variableDeclarationFragment.getInitializer());
			}

		}
	}
	
	// navigate method invocations inside method
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethodDeclarations()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			method.accept(visitor2);

			for (MethodInvocation methodInvocation : visitor2.getMethodInvocations()) {
				System.out.println("method " + method.getName() + " invoc method "
						+ methodInvocation.getName());
			}

		}
	}

	public static int getNumberOfLinesInCompilationUnit(CompilationUnit cu) {
		//On utilise les CompilationUnit au lieu des File pour respecter "l'esprit" du TP
		//On récupère la longueur de la CompilationUnit, et on récupère le numéro de ligne correspondant à la position
		//désignée par cette longueur - 1 (on enlève 1 pour être dans le fichier).
		return cu.getLineNumber(cu.getLength() - 1);
	}

	public static int getTotalNumberOfLinesInMethodsOfACompilationUnit(CompilationUnit cu) {
		//On a besoin de créer un visiteur propre à cette fonction car le visiteur de la classe contient toutes les
		//déclarations de méthodes de l'application, on ne veut que celles de la CompilationUnit courante.
		MethodDeclarationVisitor methodDeclarationVisitor = new MethodDeclarationVisitor();
		cu.accept(methodDeclarationVisitor);

		//Marqueur de positions pour calculer le nombre de ligne
		int startPosition ;
		int endPosition;
		int lengthOfCurrentMethod;

		int result = 0;

		for(MethodDeclaration method : methodDeclarationVisitor.getMethodDeclarations()) {
			startPosition = method.getStartPosition();

			endPosition = startPosition + method.getLength();

			//On récupère les numéros de ligne des positions de début et de fin pour calculer la longueur des méthodes
			//On rajoute 1 car si la différence de lignes entre le début et la fin est de 0, on a une ligne de code au total, et non 0.
			lengthOfCurrentMethod = (cu.getLineNumber(endPosition) - cu.getLineNumber(startPosition)) + 1;

			//On additionne tous les comptes de nombre de ligne
			result+=lengthOfCurrentMethod;
		}

		return result;
	}

	public static Graph<String, DefaultEdge> buildCallGraph() {

		Graph<String, DefaultEdge> callGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
		List<String> nodes = new ArrayList<>();
		List<Pair<String, String>> edges = new ArrayList<>();

		prepareNodesAndEdgesToAddToCallGraph(nodes, edges);

		addNodesAndEdgesToCallGraph(callGraph, nodes, edges);

		return callGraph;
	}

	private static void prepareNodesAndEdgesToAddToCallGraph(List<String> nodes, List<Pair<String, String>> edges) {
		//La méthode utiliser TypeDeclarationVisitor car elle devait initialement affixer le nom de la classe
		//au nom de la méthode, mais je n'ai pas réussi à faire marcher le resolveMethodBinding sur les invocations
		//de méthode afin de récupérer le receveur des envois de message, pour fusionner les nodes obtenues
		//via les MethodDeclaration et les MethodInvocation.
		//On pourrait utiliser directement un MethodDeclarationVisitor si l'on ne souhaite ajouter que les noms
		//de méthode


		//String typeDeclarationName;
		String methodDeclarationName;
		String methodInvocationName;

		for(TypeDeclaration type : typeDeclarationVisitor.getTypeDeclarations()) {
			//typeDeclarationName = type.getName().toString();

			for(MethodDeclaration method : type.getMethods()) {
				methodDeclarationName = method.getName().toString();
				nodes.add(methodDeclarationName);

				MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor();
				method.accept(methodInvocationVisitor);

				for(MethodInvocation methodInvocation : methodInvocationVisitor.getMethodInvocations()) {
					//String methodInvocationReceiverName = methodInvocation.resolveMethodBinding().getDeclaredReceiverType().getName();
					methodInvocationName = methodInvocation.getName().toString();
					nodes.add(methodInvocationName);
					edges.add(new Pair<>(methodDeclarationName, methodInvocationName));
				}
			}
		}
	}

	private static void addNodesAndEdgesToCallGraph(Graph<String, DefaultEdge> callGraph, List<String> nodes, List<Pair<String, String>> edges) {
		for(String node : nodes) {
			callGraph.addVertex(node);
		}
		for(Pair<String, String> edge : edges) {
			callGraph.addEdge(edge.getFirstElement(), edge.getSecondElement());
		}
	}
}
