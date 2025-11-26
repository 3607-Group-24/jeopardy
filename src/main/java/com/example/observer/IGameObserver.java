package com.example.observer;
import com.example.model.GameEvent;

public interface IGameObserver {
    void update(GameEvent event);
}