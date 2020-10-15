package com.rbi.loyaltysystem.repository.api;

import java.util.List;

/**
 * @author Dionysios Stolis 10/14/2020 <dstolis@b-open.com>
 */
public interface InMemory<T> {

    long add(T object);
    T findById(long id);
    void update(T object);
}
