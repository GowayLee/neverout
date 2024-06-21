package com.mambastu.view.dynamic;

import com.mambastu.view.Menu;

public interface DynamicMenu extends Menu{
    /**
     * This method is called when the menu needs to update its content.
     * 
     */
    void update();
}
