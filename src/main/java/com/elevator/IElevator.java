package com.elevator;

public interface IElevator<T> {
    void move(T o, int step);
    void load(T o);
    void unload(T o);
}
