package astvisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
	List<MethodDeclaration> methodDeclarations = new ArrayList<>();
	
	public boolean visit(MethodDeclaration node) {
		methodDeclarations.add(node);
		return super.visit(node);
	}
	
	public List<MethodDeclaration> getMethodDeclarations() {
		return methodDeclarations;
	}

	public int getMethodDeclarationsCount() { return methodDeclarations.size(); }

	public int getMaximalNumberOfParametersInMethods() {
		int result = 0;
		for(MethodDeclaration methodDeclaration : getMethodDeclarations()) {
			if(result < methodDeclaration.parameters().size()) {
				result = methodDeclaration.parameters().size();
			}
		}
		return result;
	}

}
