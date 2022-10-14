package astvisitors;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationVisitor extends ASTVisitor {
	List<TypeDeclaration> typeDeclarations = new ArrayList<>();
	
	public boolean visit(TypeDeclaration node) {
		typeDeclarations.add(node);
		return super.visit(node);
	}
	
	public List<TypeDeclaration> getTypeDeclarations() {
		return typeDeclarations;
	}

	public List<TypeDeclaration> getClassDeclarations() {
		List<TypeDeclaration> classDeclarations = new ArrayList<>();
		for(TypeDeclaration typeDeclaration : typeDeclarations) {
			if(!typeDeclaration.isInterface())
				classDeclarations.add(typeDeclaration);
		}
		return classDeclarations;
	}

	public int getTypeDeclarationsCount() { return typeDeclarations.size(); }

	public int getClassDeclarationsCount() {
		int count = 0;
		for(TypeDeclaration type : typeDeclarations) {
			if(!type.isInterface())
				count++;
		}
		return count;
	}

	public float getAverageNumberOfMethodsPerClass(){
		int totalNumberOfMethods = 0;
		for(TypeDeclaration typeDeclaration : typeDeclarations) {
			if(!typeDeclaration.isInterface()) {
				totalNumberOfMethods += typeDeclaration.getMethods().length;
			}
		}
		return (float) totalNumberOfMethods / getClassDeclarationsCount();
	}

	public float getAverageNumberOfFieldsPerClass() {
		int totalNumberOfAttributes = 0;
		for(TypeDeclaration typeDeclaration : typeDeclarations) {
			if(!typeDeclaration.isInterface()) {
				totalNumberOfAttributes += typeDeclaration.getFields().length;
			}
		}
		return (float) totalNumberOfAttributes / getClassDeclarationsCount();
	}

	public List<TypeDeclaration> getTop10PercentOfClassesWithMostMethods() {
		int top10PercentOfClassesCount = getClassDeclarationsCount() / 10;
		//Pour retourner au moins un élément
		if(top10PercentOfClassesCount == 0){
			top10PercentOfClassesCount = 1;
		}

		List<TypeDeclaration> result = new ArrayList<>();
		List<TypeDeclaration> allClassDeclarations = getClassDeclarations();

		for(TypeDeclaration classDeclaration : allClassDeclarations) {
			for(int i = 0; i < top10PercentOfClassesCount; i++) {
				if(result.size() > i) {
					if(classDeclaration.getMethods().length > result.get(i).getMethods().length){
						result.add(i, classDeclaration);

						if(result.get(top10PercentOfClassesCount) != null) {
							result.remove(top10PercentOfClassesCount);
						}
					}
				}
				else {
					result.add(i, classDeclaration);
				}
			}
		}

		return result;
	}

	public List<TypeDeclaration> getTop10PercentOfClassesWithMostFields() {
		int top10PercentOfClassesCount = getClassDeclarationsCount() / 10;
		//Pour retourner au moins un élément
		if(top10PercentOfClassesCount == 0){
			top10PercentOfClassesCount = 1;
		}

		List<TypeDeclaration> result = new ArrayList<>();
		List<TypeDeclaration> allClassDeclarations = getClassDeclarations();

		for(TypeDeclaration classDeclaration : allClassDeclarations) {
			for(int i = 0; i < top10PercentOfClassesCount; i++) {
				if(result.size() > i) {
					if(classDeclaration.getFields().length > result.get(i).getFields().length){
						result.add(i, classDeclaration);

						if(result.get(top10PercentOfClassesCount) != null) {
							result.remove(top10PercentOfClassesCount);
						}
					}
				}
				else {
					result.add(i, classDeclaration);
				}
			}
		}

		return result;
	}

	public List<TypeDeclaration> getTop10PercentOfClassesWithMostMethodsAndFields() {
		List<TypeDeclaration> leftAnd = getTop10PercentOfClassesWithMostMethods();
		List<TypeDeclaration> rightAnd = getTop10PercentOfClassesWithMostFields();

		leftAnd.retainAll(rightAnd);

		return leftAnd;
	}

	public List<TypeDeclaration> getClassesWithMoreThanXMethods(int x) {
		List<TypeDeclaration> result = new ArrayList<>();

		for(TypeDeclaration typeDeclaration : getClassDeclarations()) {
			if(typeDeclaration.getMethods().length > x) {
				result.add(typeDeclaration);
			}
		}

		return result;
	}

	public String getTypeDeclarationsNamesAsString() {
		String result = "[ ";
		for(TypeDeclaration t : typeDeclarations) {
			result += t.getName() + " ";
		}
		result+="]";
		return result;
	}
}
