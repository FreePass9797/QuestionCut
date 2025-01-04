package com.lightning.edu.ei.edgealgorithm;

public class Params {
    public String blur_url;
    public String dark_url;
    public boolean debug;
    public boolean only_enable_orientation;
    public String orientation_url;
    public String questions_url;
    public String scene_url;
    public boolean test;
    public double blur_threshold = 3.2d;
    public boolean enable_blur = true;
    public boolean enable_scene = true;
    public boolean enable_orientation = true;
    public boolean enable_dark = true;
    public int blur_status = 1;
    public int scene_status = 1;
    public int dark_status = 1;
    public int orientation_status = 1;
    public int questions_status = 1;

    @Override
    public String toString() {
        return "Params{" +
                "blur_url='" + blur_url + '\'' +
                ", dark_url='" + dark_url + '\'' +
                ", debug=" + debug +
                ", only_enable_orientation=" + only_enable_orientation +
                ", orientation_url='" + orientation_url + '\'' +
                ", questions_url='" + questions_url + '\'' +
                ", scene_url='" + scene_url + '\'' +
                ", test=" + test +
                ", blur_threshold=" + blur_threshold +
                ", enable_blur=" + enable_blur +
                ", enable_scene=" + enable_scene +
                ", enable_orientation=" + enable_orientation +
                ", enable_dark=" + enable_dark +
                ", blur_status=" + blur_status +
                ", scene_status=" + scene_status +
                ", dark_status=" + dark_status +
                ", orientation_status=" + orientation_status +
                ", questions_status=" + questions_status +
                '}';
    }
}
