package transform

import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.ast.*
import static org.objectweb.asm.Opcodes.ACC_FINAL
import static org.objectweb.asm.Opcodes.ACC_PUBLIC

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
public class KoanPlaceholdersAstTransformation implements ASTTransformation {
    private static final FILL_ME_IN = new ConstantExpression('fill_me_in')
    private static final OBJECT = new ClassNode(Object)
    private static final NO_EXCEPTIONS = [] as ClassNode[]
    private static final NO_PARAMS = [] as Parameter[]
    private static final NO_VARIABLE_SCOPE = null

    @Override
    void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (!nodes) return
        if (nodes.size() < 2) return
        if (!(nodes[0] instanceof AnnotationNode)) return
        if (!(nodes[1] instanceof ClassNode)) return

        ClassNode classNode = nodes[1]
        classNode.addMethod(buildMethodPlaceholder())
        classNode.addField(buildFieldPlaceholder(classNode))
    }

    private buildMethodPlaceholder() {
        def methodBody = new BlockStatement([new ReturnStatement(FILL_ME_IN)], NO_VARIABLE_SCOPE)
        new MethodNode('__', ACC_PUBLIC, OBJECT, NO_PARAMS, NO_EXCEPTIONS, methodBody)
    }

    private buildFieldPlaceholder(owner) {
        new FieldNode('__', ACC_PUBLIC | ACC_FINAL, OBJECT, owner, FILL_ME_IN)
    }
}