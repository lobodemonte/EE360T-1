package hw1;

import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;


public class GraphGenerator {
	Graph hierarchyGraph = new Graph();
	Graph callGraph = new Graph();
	ASTParser parser = ASTParser.newParser(AST.JLS4);
	List<CompilationUnit> units = new ArrayList<CompilationUnit>();
	List<String> sourcePath = new ArrayList<String>();
	List<String> sourceFile = new ArrayList<String>();
	// other possible fields are omitted -- feel free to add them if you need to
	String callingClass;
	public GraphGenerator() {
		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	}

	void createGraph() {
		for(final CompilationUnit cu : units) {
			
			cu.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					String name = node.resolveBinding().getQualifiedName();
					callingClass = name;
					hierarchyGraph.addNode(name);
					
					if (node.getSuperclassType() != null){
						String superName = node.getSuperclassType().resolveBinding().getQualifiedName();
						hierarchyGraph.addEdge(superName, name);
					}
					else{
						hierarchyGraph.addEdge("java.lang.Object", name);
					}
					
					// ...
					return super.visit(node);
				}
				@Override
				public boolean visit(MethodDeclaration node) {
					String name = node.resolveBinding().toString();
					callGraph.addNode(name);
					
					
					if (callingClass != null){
						callGraph.addEdge(callingClass, name);
						
					}
					// ...
					return super.visit(node);
				}
				@Override
				public boolean visit(MethodInvocation node) {
					String name = node.resolveMethodBinding().getName();
					callGraph.addNode(name);
					
					// ...
					return super.visit(node);
				}
			});
		}
		// Fill in the code here to complete the static call graph.
		// We cannot determine which method is invoked at runtime,
		// so we need to add edges to all possible invoked methods.
		// ...
	}
	public void generateASTs(String[] classpathEntries,
			String[] sourcepathEntries, final String[] sourceFilePaths) {
		FileASTRequestor requestor = new FileASTRequestor() {
			public void acceptAST(String sourceFilePath, CompilationUnit ast) {
				units.add(ast);
			}
		};
		parser.setEnvironment(classpathEntries, sourcepathEntries, null, true);
		parser.createASTs(sourceFilePaths, null, new String[0], requestor, null);
	}
	public void preParsingProcess(String dir) throws IOException {
		File directory = new File(dir);
		if (!directory.exists() || directory.isFile()) {
			throw new IOException("Cannot find target directory.");
		}
		sourcePath.add(dir);
		for (File f : directory.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".java")) {
				sourceFile.add(f.getAbsolutePath());
			}
		}
	}
	public static void main(String[] args) throws IOException {
		if(args.length != 1) {
			System.out.println("Expect one argument.");
			System.exit(1);
		}
		GraphGenerator gg = new GraphGenerator();
		gg.preParsingProcess(args[0]);
		gg.generateASTs(null, (String[]) gg.sourcePath.toArray(new String[0]),
				(String[]) gg.sourceFile.toArray(new String[0]));
		gg.createGraph();
		gg.hierarchyGraph.printGraph();
		gg.callGraph.printGraph();
	}
}
