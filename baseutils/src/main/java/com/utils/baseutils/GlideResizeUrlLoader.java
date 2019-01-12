package com.utils.baseutils;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

/**
 * Created by huangyifei on 16/9/20.
 */
public class GlideResizeUrlLoader extends BaseGlideUrlLoader<GlideResizeStudio> {
    public GlideResizeUrlLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(GlideResizeStudio model, int width, int height) {
        return model.requestCustomSizeUrl(width, height);
    }
}
