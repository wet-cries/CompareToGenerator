import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.example.GenerateCompareToAction;
import org.example.ui.PsiFieldWithSortedOrder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenerateTest extends LightJavaCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(GenerateTest.class);
        assert testOutput != null;
        File sourceRoot = new File(testOutput);
        sourceRoot = sourceRoot.getParentFile().getParentFile().getParentFile().getParentFile();
//        sourceRoot = sourceRoot.getParentFile().getParentFile().getParentFile().getParentFile();
//        return new File(sourceRoot, "src/test/testData").getPath();
//        return sourceRoot.getPath();
        return new File(sourceRoot, "src/test/testData").getPath();
    }

    public void testSimple() {
        myFixture.configureByFile("beforeSimple.java");
        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiClass simpleClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);
        assert simpleClass != null;
        PsiField foo = simpleClass.findFieldByName("foo", false);
        List<PsiFieldWithSortedOrder> fields = new ArrayList<>();
        fields.add(new PsiFieldWithSortedOrder(foo, true));
        WriteCommandAction.runWriteCommandAction(myFixture.getProject(), () -> {
            new GenerateCompareToAction().generate(simpleClass, fields);
        });
        myFixture.checkResultByFile("afterSimple.java");
    }

    public void testNumber() {
        myFixture.configureByFile("beforeNumber.java");
        PsiElement elementAtCaret = myFixture.getFile().findElementAt(myFixture.getCaretOffset());
        PsiClass simpleClass = PsiTreeUtil.getParentOfType(elementAtCaret, PsiClass.class);
        assert simpleClass != null;
        PsiField foo = simpleClass.findFieldByName("number", false);
        List<PsiFieldWithSortedOrder> fields = new ArrayList<>();
        fields.add(new PsiFieldWithSortedOrder(foo, true));
        WriteCommandAction.runWriteCommandAction(myFixture.getProject(), () -> {
            new GenerateCompareToAction().generate(simpleClass, fields);
        });
        myFixture.checkResultByFile("afterNumber.java");
    }
}
