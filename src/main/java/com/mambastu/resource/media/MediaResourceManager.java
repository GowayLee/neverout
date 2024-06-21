package com.mambastu.resource.media;

import com.mambastu.resource.ResourceManager;

public interface MediaResourceManager extends ResourceManager{
    /**
     * Loads all media resource from static JSON file.
     */
    void loadResources();

    /**
     * Clears all media resources from cache.
     * 
     */
    void clearCache();
}
