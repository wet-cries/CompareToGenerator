package org.example;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ThrowableRunnable;
import org.example.ui.GenerateDialog;
import org.example.ui.PsiFieldWithSortedOrder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenerateCompareToAction extends AnAction {
    private final String dialogTitle;

    public GenerateCompareToAction() {
        this("Generate CompareTo()", "Generate CompareTo()");
    }

    public GenerateCompareToAction(String text, String dialogTitle) {
        super(text);
        this.dialogTitle = dialogTitle;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromContext(e);
        GenerateDialog dialog = new GenerateDialog(psiClass, dialogTitle);
        dialog.show();
        if (dialog.isOK()) {
            generate(psiClass, dialog.getMyFields());
        }

    }

    public void generate(final PsiClass psiClass, final List<PsiFieldWithSortedOrder> fields) {
//        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {
//            @Override
//            protected void run() throws Throwable {
//                generateCompareTo(psiClass, fields);
//                generateImplementsComparable(psiClass);
//            }
//
//
//        }.execute();

        try {
            WriteCommandAction.writeCommandAction(psiClass.getProject()).run(
                    (ThrowableRunnable<Throwable>) () -> {
                        generateCompareTo(psiClass, fields);
                        generateImplementsComparable(psiClass);
                    }
            );
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void generateImplementsComparable(PsiClass psiClass) {
        PsiClassType[] implementsListTypes = psiClass.getImplementsListTypes();

        boolean implementsComparable = false;
        for (PsiClassType implementsListType : implementsListTypes) {
            PsiClass resolvedClass = implementsListType.resolve();
            if (resolvedClass != null && "java.lang.Comparable".equals(resolvedClass.getQualifiedName())) {
                implementsComparable = true;
                break;
            }
        }

        if (!implementsComparable) {
            String comparableText = "Comparable<" + psiClass.getName() + ">";
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
            PsiJavaCodeReferenceElement referenceElement
                    = elementFactory.createReferenceFromText(comparableText, psiClass);
            psiClass.getImplementsList().add(referenceElement);
        }
    }

    private static final String[][] comparisonOrder = {{"<", ">"}, {">", "<"}};

    public void generateCompareTo(PsiClass psiClass, List<PsiFieldWithSortedOrder> fields) {
        StringBuilder builder = new StringBuilder("@Override\n");
        builder.append("public int compareTo(").append(psiClass.getName()).append(" that) { \n");

        for (int i = 0; i < fields.size(); i++) {
            PsiField field = fields.get(i).getPsiField();
            boolean ascending = fields.get(i).isAscending();
            int index = ascending ? 0 : 1;
            String[] comparisons = comparisonOrder[index];
            PsiType type = field.getType();
            PsiAnnotation annotationNotNull = field.getAnnotation("org.jetbrains.annotations.NotNull");
            @NotNull PsiAnnotation[] annotations = field.getAnnotations();

            if (i != 0) {
                builder.append("\n");
            }




            if (type.equals(PsiType.BOOLEAN) ||
                    type.equals(PsiType.BYTE) ||
                    type.equals(PsiType.INT) ||
                    type.equals(PsiType.CHAR) ||
                    type.equals(PsiType.DOUBLE) ||
                    type.equals(PsiType.FLOAT) ||
                    type.equals(PsiType.LONG) ||
                    type.equals(PsiType.SHORT)) {
                builder.append("if (this." + field.getName() + " " + comparisons[0] + " that." + field.getName() + ")" + " {\n");
                builder.append("  return -1;\n");
                builder.append("} else if (this." + field.getName() + " " + comparisons[1] + " that." + field.getName() + ") {\n");
                builder.append("  return 1;\n");
                builder.append("}\n");
            } else {
                if (annotationNotNull == null) {
                    builder.append("if (this." + field.getName() + " != null && that." + field.getName() + " != null) {\n");
                    builder.append("    if (this." + field.getName() + ".compareTo(that." + field.getName() + ") " + comparisons[0] + " 0) {\n");
                    builder.append("    return -1;\n");
                    builder.append("    } else if (this." + field.getName() + ".compareTo(that." + field.getName() + ") " + comparisons[1] + " 0) {\n");
                    builder.append("    return 1;\n");
                    builder.append("    }\n");
                    builder.append("    } else if (this." + field.getName() + " != null) {\n");
                    builder.append("        return 1;\n");
                    builder.append("    } else if (that." + field.getName() + " != null) {\n");
                    builder.append("    return -1;\n");
                    builder.append(("}\n"));
                } else {
                    builder.append("    if (this." + field.getName() + ".compareTo(that." + field.getName() + ") " + comparisons[0] + " 0) {\n");
                    builder.append("    return -1;\n");
                    builder.append("    } else if (this." + field.getName() + ".compareTo(that." + field.getName() + ") " + comparisons[1] + " 0) {\n");
                    builder.append("    return 1;\n");
                    builder.append("    }\n");
                }
            }
        }

        builder.append("\n");
        builder.append("return 0;\n");
        builder.append("}\n");
        setNewMethod(psiClass, builder.toString(), "compareTo");
    }

    private void setNewMethod(PsiClass psiClass, String newMethodBody, String methodName) {
        PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        PsiMethod newEqualsMethod = elementFactory.createMethodFromText(newMethodBody, psiClass);
        PsiElement method = addOrReplaceMethod(psiClass, newEqualsMethod, methodName);
        JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(method);
    }

    private PsiElement addOrReplaceMethod(PsiClass psiClass, PsiMethod newEqualsMethod, String methodName) {
        PsiMethod existingEqualsMethod = findMethod(psiClass, methodName);
        PsiElement method;
        if (existingEqualsMethod != null) {
            method = existingEqualsMethod.replace(newEqualsMethod);
        } else {
            method = psiClass.add(newEqualsMethod);
        }

        return method;
    }

    private PsiMethod findMethod(PsiClass psiClass, String methodName) {
        PsiMethod[] allMethods = psiClass.getAllMethods();
        for (PsiMethod method : allMethods) {
            if (psiClass.getName().equals(method.getContainingClass().getName()) && methodName.equals(method.getName())) {
                return method;
            }
        }
        return null;
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (psiFile == null || editor == null) {
            return null;
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
