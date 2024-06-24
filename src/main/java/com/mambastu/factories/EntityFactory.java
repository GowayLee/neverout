package com.mambastu.factories;

public interface EntityFactory<T, V extends Enum<V>> {
    /**
     * Borrow an instance from the embeded pool. If there is no instance available, a new one will be created.
     * 
     * @param type
     * @return an instance of the entity.
     */
    T create(V type);

    /**
     * Return an instance to the embeded pool
     *
     * @param obj the instance to return.
     */
    void delete(T obj);
}