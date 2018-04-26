package com.abacus.poc295.base;

/**
 * @author Aaditya deowanshi
 */
public interface Presenter<T extends ViewInteractor> {

    void attachViewInteractor(T viewInteractor);

    void detachViewInteractor();

}
