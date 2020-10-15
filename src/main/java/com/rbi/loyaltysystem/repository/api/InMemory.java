package com.rbi.loyaltysystem.repository.api;

public interface InMemory<T> {

    long add(T object);
    T findById(long id);
}
