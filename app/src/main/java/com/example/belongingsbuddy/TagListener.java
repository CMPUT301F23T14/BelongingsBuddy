package com.example.belongingsbuddy;

import java.io.Serializable;
import java.util.ArrayList;

public interface TagListener extends Serializable {
    public void tagListen(ArrayList<Tag> tagList);
}
