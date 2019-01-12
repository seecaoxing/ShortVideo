package com.utils.baseutils;

/**
 * Created by huangyifei on 16/9/20.
 */
public class GlideResizeStudio {
    String baseImageUrl;

    public GlideResizeStudio(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }

    public String requestCustomSizeUrl(int w, int h) {
        if (baseImageUrl == null) {
            return null;
        }
        if (baseImageUrl.contains("nos.netease.com")) {
            if (baseImageUrl.contains("imageView")) {
                if (baseImageUrl.endsWith("imageView")) {
                    return baseImageUrl + "&" + getSuffix(w, h);
                } else if (baseImageUrl.endsWith("imageView&")) {
                    return baseImageUrl + getSuffix(w, h);
                } else {
                    return baseImageUrl;
                }
            } else {
                return baseImageUrl + "?imageView&" + getSuffix(w, h);
            }
        }
        return baseImageUrl;
    }

    public static String getSuffix(int w, int h) {
        return "thumbnail=" + w + "x" + h + "&tostatic=0";
    }

}
