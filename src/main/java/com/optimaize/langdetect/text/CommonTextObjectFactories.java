package com.optimaize.langdetect.text;

/**
 * Contains some standard {@link com.optimaize.langdetect.text.TextObjectFactory}s ready to use for
 * common use cases.
 *
 * @author Fabian Kessler
 */
public class CommonTextObjectFactories {

    public static TextObjectFactory forDetectingOnLargeText() {
        return new TextObjectFactoryBuilder()
                .maxTextLength(10000)
                .withTextFilter(UrlTextFilter.getInstance())
                .withTextFilter(RemoveMinorityScriptsTextFilter.forThreshold(0.3))
                .build();
    }

    public static TextObjectFactory forDetectingShortCleanText() {
        return new TextObjectFactoryBuilder()
                .build();
    }

    public static TextObjectFactory forIndexing() {
        return new TextObjectFactoryBuilder()
                .withTextFilter(UrlTextFilter.getInstance())
                .withTextFilter(RemoveMinorityScriptsTextFilter.forThreshold(0.3))
                .build();
    }

    public static TextObjectFactory forIndexingCleanText() {
        return new TextObjectFactoryBuilder()
                .build();
    }

    public static TextObjectFactory forInstagramPosts() {
        return new TextObjectFactoryBuilder()
                .withTextFilter(UrlTextFilter.getInstance())
                .build();
    }

}
