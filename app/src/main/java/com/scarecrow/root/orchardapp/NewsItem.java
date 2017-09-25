package com.scarecrow.root.orchardapp;

import java.io.Serializable;

/**
 * Created by root on 17-9-25.
 */
public class NewsItem implements Serializable {
    public String title, thumbnail_url, image_url, long_text;
    public Integer type, id, view_times;
    //  0 -> healthy , 1 -> knowledge
}
