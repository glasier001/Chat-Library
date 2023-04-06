package com.commonlib.typefacehandler;

import android.content.Context;
import android.graphics.Typeface;

import com.commonlib.constants.AppStrings;


/**
 * Created by sagar on 9/5/17.
 */

public class TypefaceUtils {


    public static final int INT_CODE_REGULAR_FONT = 0;
    public static final int INT_CODE_BOLD_FONT = 1;
    public static final int INT_CODE_LIGHT_FONT = 2;
    public static final int INT_CODE_LIGHT_ITALIC_FONT = 3;
    public static final int INT_CODE_DEMI_BOLD = 4;
    public static final int INT_CODE_MEDIUM = 5;
    public static final int INT_CODE_MEDIUM_ITALIC = 6;
    public static final int INT_CODE_REGULAR_ITALIC = 7;

    private static final String REGULAR_FONT = "avenir_next_regular.ttf";
    private static final String BOLD_FONT = "avenir_next_bold.ttf";
    private static final String DEMI_BOLD = "avenir_next_demi_bold.ttf";
    private static final String MEDIUM = "avenir_next_medium.ttf";
//    private static final String MEDIUM_ITALIC = "avenirnext_MediumItalic.ttf";

//    private static final String LIGHT_FONT = "HelveticaNeue-Light.otf";
//    private static final String LIGHT_ITALIC_FONT = "HelveticaNeue-LightItalic.otf";
    private static final String REGULAR_ITALIC = "avenir_next_italic.ttf";

    private static volatile TypefaceUtils mTypefaceUtils;
    private static volatile Typeface regularTypeface;
    private static volatile Typeface boldTypeface;
//    private static volatile Typeface lightTypeface;
//    private static volatile Typeface lightItalicTypeface;
    private static volatile Typeface demiBoldTypeface;
    private static volatile Typeface mediumTypeface;
//    private static volatile Typeface mediumItalicTypeface;
    private static volatile Typeface regularItalicTypeface;


    //Private constructor
    private TypefaceUtils() {
        //Prevent from the reflection
        if (mTypefaceUtils != null) {
            throw new RuntimeException(AppStrings.STR_MSG_ERROR_TYPEFACE_REFLECTION);
        }
    }

    /**
     * Gives {@link #mTypefaceUtils} instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile {@link #mTypefaceUtils}
     * see {@link #mTypefaceUtils}
     * @since 1.0
     */
    public static TypefaceUtils getInstance() {
        if (mTypefaceUtils == null) {
            synchronized (TypefaceUtils.class) {
                if (mTypefaceUtils == null) {
                    mTypefaceUtils = new TypefaceUtils();
                }
            }
        }
        return mTypefaceUtils;
    }

    /**
     * Gives {@link #boldTypeface} instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile {@link #boldTypeface}
     * see {@link #boldTypeface}
     * @since 1.0
     */
    public Typeface getBoldTypeface(Context mContext) {
        if (boldTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (boldTypeface == null) {
                    boldTypeface = Typeface.createFromAsset(mContext.getAssets(), BOLD_FONT);
                }
            }
        }
        return boldTypeface;
    }

    /**
     * Gives {@link #regularTypeface} instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile {@link #regularTypeface}
     * see {@link #regularTypeface}
     * @since 1.0
     */
    public Typeface getRegularTypeface(Context mContext) {
        if (regularTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (regularTypeface == null) {
                    regularTypeface = Typeface.createFromAsset(mContext.getAssets(), REGULAR_FONT);
                }
            }
        }
        return regularTypeface;
    }

    /**
     * Gives {@link #regularItalicTypeface} instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile {@link #regularItalicTypeface}
     * see {@link #regularItalicTypeface}
     * @since 1.0
     */
    public Typeface getRegularItalicTypeface(Context mContext) {
        if (regularItalicTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (regularItalicTypeface == null) {
                    regularItalicTypeface = Typeface.createFromAsset(mContext.getAssets(), REGULAR_ITALIC);
                }
            }
        }
        return regularItalicTypeface;
    }

    /**
     * Gives {@link #lightTypeface} instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile {@link #lightTypeface}
     * see {@link #lightTypeface}
     * @since 1.0
     *//*
    public Typeface getLightTypeface(Context mContext) {
        if (lightTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (lightTypeface == null) {
                    lightTypeface = Typeface.createFromAsset(mContext.getAssets(), LIGHT_FONT);
                }
            }
        }
        return lightTypeface;
    }

    *//**
     * Gives {@link #} instance of this class with secure singleton pattern
     * <p>
     * Uses thread safety and double check lock on volatile return type
     * <p>
     *
     * @return singleton and volatile {@link #}
     * see {@link #}
     * @since 1.0
     *//*
    public Typeface getLightItalicTypeface(Context mContext) {
        if (lightItalicTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (lightItalicTypeface == null) {
                    lightItalicTypeface = Typeface.createFromAsset(mContext.getAssets(), LIGHT_ITALIC_FONT);
                }
            }
        }
        return lightItalicTypeface;
    }*/

    public Typeface getDemiBoldTypeface(Context mContext) {
        if (demiBoldTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (demiBoldTypeface == null) {
                    demiBoldTypeface = Typeface.createFromAsset(mContext.getAssets(), DEMI_BOLD);
                }
            }
        }
        return demiBoldTypeface;
    }

    public Typeface getMediumTypeface(Context mContext) {
        if (mediumTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (mediumTypeface == null) {
                    mediumTypeface = Typeface.createFromAsset(mContext.getAssets(), MEDIUM);
                }
            }
        }
        return mediumTypeface;
    }

    /*public Typeface getMediumItalicTypeface(Context mContext) {
        if (mediumItalicTypeface == null) {
            synchronized (TypefaceUtils.class) {
                if (mediumItalicTypeface == null) {
                    mediumItalicTypeface = Typeface.createFromAsset(mContext.getAssets(), MEDIUM_ITALIC);
                }
            }
        }
        return mediumItalicTypeface;
    }*/
}
