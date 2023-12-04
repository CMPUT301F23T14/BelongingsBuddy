package com.example.belongingsbuddy;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The TagListener interface provides a callback method for classes which require tag selection
 */
public interface TagListener extends Serializable {
    public void tagListen(ArrayList<Tag> tagList);
}
