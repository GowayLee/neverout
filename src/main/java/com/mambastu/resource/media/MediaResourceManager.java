package com.mambastu.resource.media;

import com.mambastu.resource.ResourceManager;

public interface MediaResourceManager extends ResourceManager{
    void loadResources();

    void clearCache();
}
