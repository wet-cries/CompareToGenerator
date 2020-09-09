package org.example.ui;

import com.intellij.psi.PsiField;

public class PsiFieldWithSortedOrder {
    private final PsiField psiField;
    private boolean ascending;

    public PsiFieldWithSortedOrder(PsiField psiField, boolean ascending) {
        this.psiField = psiField;
        this.ascending = ascending;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return  true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PsiFieldWithSortedOrder that = (PsiFieldWithSortedOrder) obj;

        if (ascending != that.ascending) return false;
        return psiField.equals(that.psiField);
    }

    @Override
    public int hashCode() {
        int result = psiField.hashCode();
        result = 31 * result + (ascending ? 1 : 0);
        return result;
    }
}
