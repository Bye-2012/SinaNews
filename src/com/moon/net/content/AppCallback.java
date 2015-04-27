package com.moon.net.content;

public interface AppCallback<T> {
	void callback(T t);
	void onError(Exception e);
}
