package com.bbking.prcessor;

import com.bbking.prcessor.annotation.FConfig;
import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 *
 * @author gangwen.xu
 * Date  : 2018/5/8
 * Time  : 下午6:09
 * 类描述 :
 */
@AutoService(Process.class)
@SupportedAnnotationTypes("*")
public class SelfAnnotationProcessor extends AbstractProcessor {

    /**
     * Messager主要是用来在编译期打log用的
     * JavacTrees提供了待处理的抽象语法树
     * TreeMaker封装了创建AST节点的一些方法
     * Names提供了创建标识符的方法
     */
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv){
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(FConfig.class)) {
            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                        }
                    }
                    for (JCTree.JCVariableDecl jcVariableDecl :jcVariableDeclList){
                        messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
                        jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
                    }
                    super.visitClassDef(jcClassDecl);
                }

            });


        }
        return false;
    }


    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {

        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                getNewMethodName(jcVariableDecl.getName()),
                jcVariableDecl.vartype, List.nil(), List.nil(), List.nil(), body, null);
    }

    private Name getNewMethodName(Name name) {
        String s = name.toString();
        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }



    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
