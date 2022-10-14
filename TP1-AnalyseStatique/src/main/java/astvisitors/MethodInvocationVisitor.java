package astvisitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

public class MethodInvocationVisitor extends ASTVisitor {
	List<MethodInvocation> methodInvocations = new ArrayList<>();
	List<SuperMethodInvocation> superMethodInvocations = new ArrayList<>();
	public boolean visit(MethodInvocation node) {
		methodInvocations.add(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethodInvocations.add(node);
		return super.visit(node);
	}

	
	public List<MethodInvocation> getMethodInvocations() {
		return methodInvocations;
	}
	
	public List<SuperMethodInvocation> getSuperMethodInvocations() {
		return superMethodInvocations;
	}

}
