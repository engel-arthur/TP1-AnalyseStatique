package astvisitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.ArrayList;
import java.util.List;

public class PackageDeclarationVisitor extends ASTVisitor {
    List<PackageDeclaration> packages = new ArrayList<>();

    public boolean visit(PackageDeclaration node) {
        packages.add(node);
        return super.visit(node);
    }

    public List<PackageDeclaration> getPackages() { return packages; }

    public int getNumberOfPackages() { return packages.size(); }
}
