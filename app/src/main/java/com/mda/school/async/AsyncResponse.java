package com.mda.school.async;

/**
 * Created by michael on 22/11/17.
 */

public interface AsyncResponse<T> {
    void processFinish(T output);
}
