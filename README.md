# language-detector

Language Detection Library for Java

    <dependency>
        <groupId>com.optimaize.languagedetector</groupId>
        <artifactId>language-detector</artifactId>
        <version>0.5</version>
    </dependency>


## Language Support

### 70 Built-in Language Profiles

1. af Afrikaans
1. an Aragonese
1. ar Arabic
1. ast Asturian
1. be Belarusian
1. br Breton
1. ca Catalan
1. bg Bulgarian
1. bn Bengali
1. cs Czech
1. cy Welsh
1. da Danish
1. de German
1. el Greek
1. en English
1. es Spanish
1. et Estonian
1. eu Basque
1. fa Persian
1. fi Finnish
1. fr French
1. ga Irish
1. gl Galician
1. gu Gujarati
1. he Hebrew
1. hi Hindi
1. hr Croatian
1. ht Haitian
1. hu Hungarian
1. id Indonesian
1. is Icelandic
1. it Italian
1. ja Japanese
1. km Khmer
1. kn Kannada
1. ko Korean
1. lt Lithuanian
1. lv Latvian
1. mk Macedonian
1. ml Malayalam
1. mr Marathi
1. ms Malay
1. mt Maltese
1. ne Nepali
1. nl Dutch
1. no Norwegian
1. oc Occitan
1. pa Punjabi
1. pl Polish
1. pt Portuguese
1. ro Romanian
1. ru Russian
1. sk Slovak
1. sl Slovene
1. so Somali
1. sq Albanian
1. sr Serbian
1. sv Swedish
1. sw Swahili
1. ta Tamil
1. te Telugu
1. th Thai
1. tl Tagalog
1. tr Turkish
1. uk Ukrainian
1. ur Urdu
1. vi Vietnamese
1. yi Yiddish
1. zh-cn Simplified Chinese
1. zh-tw Traditional Chinese


User danielnaber has made available a profile for Esperanto on his website, see open tasks.


### Other Languages

You can create a language profile for your own language easily.
See https://github.com/optimaize/language-detector/blob/master/src/main/resources/README.md


## How it Works

The software uses language profiles which were created based on common text for each language.
N-grams http://en.wikipedia.org/wiki/N-gram were then extracted from that text, and that's what is stored in the profiles.

When trying to figure out in what language a certain text is written, the program goes through the same process:
It creates the same kind of n-grams of the input text. Then it compares the relative frequency of them, and finds the
language that matches best.


### Challenges

This software does not work as well when the input text to analyze is short, or unclean. For example tweets.

When a text is written in multiple languages, the default algorithm of this software is not appropriate.
You can try to split the text (by sentence or paragraph) and detect the individual parts. Running the language guesser
on the whole text will just tell you the language that is most dominant, in the best case.

This software cannot handle it well when the input text is in none of the expected (and supported) languages.
For example if you only load the language profiles from English and German, but the text is written in French,
the program may pick the more likely one, or say it doesn't know. (An improvement would be to clearly detect that
it's unlikely one of the supported languages.)

If you are looking for a language detector / language guesser library in Java, this seems to be the best open source
library you can get at this time. If it doesn't need to be Java, you may want to take a look at https://code.google.com/p/cld2/


## How to Use

#### Language Detection for your Text

    //load all languages:
    List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

    //build language detector:
    LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
            .withProfiles(languageProfiles)
            .build();

    //create a text object factory
    TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

    //query:
    TextObject textObject = textObjectFactory.forText("my text");
    Optional<LdLocale> lang = languageDetector.detect(textObject);


#### Creating Language Profiles for your Training Text

    //create text object factory:
    TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexingCleanText();

    //load your training text:
    TextObject inputText = textObjectFactory.create()
            .append("this is my")
            .append("training text")

    //create the profile:
    LanguageProfile languageProfile = new LanguageProfileBuilder("en")
            .ngramExtractor(NgramExtractors.standard())
            .minimalFrequency(5) //adjust please
            .addText(inputText)
            .build();

    //store it to disk if you like:
    new LanguageProfileWriter().writeToDirectory(languageProfile, "c:/foo/bar");


For the profile name, use he ISO 639-1 language code if there is one, otherwise the ISO 639-3 code.

The training text should be rather clean; it is a good idea to remove parts written in other languages
(like English phrases, or Latin script content in a Cyrillic text for example). Some also like to remove
proper nouns like (international) place names in case there are too many. It's up to you how far you go.
As a general rule, the cleaner the text is, the better is its profile.
If you scrape text from Wikipedia then please only use the main content, without the left side navigation etc.

The profile size should be similar to the existing profiles for practical reasons. To compute the likeliness
for an identified language, the index size is put in relation, therefore a language with a larger profile
won't have a higher probability to be chosen.

Please contribute your new language profile to this project. The file can be added to the languages folder, and
then referenced in the BuiltInLanguages class. Or else open a ticket, and provide a download link.

Also, it's a good idea to put the original text along with the modifying (cleaning) code into a new
project on GitHub. This gives others the possibility to improve on your work. Or maybe even use the
training text in other, non-Java software.


## How You Can Help

If your language is not supported yet, then you can provide clean "training text", that is, common text written in your
language. The text should be fairly long (a couple of pages at the very least). If you can provide that, please open
a ticket.

If your language is supported already, but not identified clearly all the time, you can still provide such training
text. We might then be able to improve detection for your language.

If you're a programmer, dig in the source and see what you can improve. Check the open tasks.


## History and Changes

This is a fork from https://code.google.com/p/lang-guess/ (forked on 2014-02-27) which itself is a fork
of the original project https://code.google.com/p/language-detection/

#### Changes made here

##### Functional Changes

* Made results for short text consistent, no random n-gram selection for short text (configurable).
* Configurable when to remove ASCII (foreign script). Old code did it when ascii was less than 1/3 of the content, and only for ASCII.
* New n-gram generation. Faster, and flexible (filter, space-padding). Previously it was hardcoded to 1, 2 and 3-grams, and it had hardcoded which n-grams were ignored.
* LanguageDetector is now safe to use multi-threaded.
* Clear code to safely load profiles and use them, no state in static fields.
* Easier to generate your own language profiles based on training text, and to load and store them.
* Feature to weight prefix and suffix n-grams higher.

##### Technical Changes

* Updated to use Java 7 for compilation, and for syntax. It's 2015, and 7/8 are the only officially supported version by Oracle.
* Code quality improvements:
  * Returning interfaces instead of implementations (List instead of ArrayList etc)
  * String .equals instead of ==
  * Replaced StringBuffer with StringBuilder
  * Renamed classes for clarity
  * Made classes immutable, and thus thread safe
  * Made fields private, using accessors
  * Clear null reference concept:
    * using IntelliJ's @Nullable and @NotNull annotations
    * using Guava's Optional
  * Added JavaDoc, fixed typos
  * Added interfaces
  * More tests. Thanks to the refactorings, code is now testable that was too much embedded before.
* Removed the "seed" completely (for the Random() number generator, I don't see the use). UPDATE: now I do, there's an open task to re-add this.
* Updated all Maven dependency versions
* Replaced last lib dependency with Maven (jsonic)

##### Legal

Apache2 license, just like the work from which this is derived.
(I had temporarily changed it to LGPLv3, but that change was invalid and therefore reverted.)


##### TODO

The software works well, there are things that can be improved. Check the Issues list.


#### Why so much forking?

The original project hasn't seen any commit in a while. The issue list is growing.
The news page says for 2012 that it now has Maven support, but there is no pom in git.
There is a release in Maven see http://mvnrepository.com/artifact/com.cybozu.labs/langdetect/1.1-20120112
for version 1.1-20120112 but not in git. So I don't know what's going on there.

The lang-guess fork saw quite some commits in 2011 and up to march 2012, then nothing anymore.
It uses Maven.

The 2 projects are not in sync, it looks like they did not integrate changes from each other anymore.

Both are on Google Code, I believe that GitHub is a much better place for contributing.

My goals were to bring the code up to current standards, and to update it for Java 7. So I quickly
noticed that I have to touch pretty much all code. And with the status of the other two projects,
I figured that I better start my own. This ensures that my work is published to the public.



## Where it's used

An adapted version of this is used by the http://www.NameAPI.org server.

https://www.languagetool.org/ is a proof-reading software for LibreOffice/OpenOffice, for the Desktop and for Firefox.



## License

Apache 2 (business friendly)



## Authors

###### Nakatani Shuyo

* Started the project and built most of the functionality. Provided the language profiles.
* Project is at https://code.google.com/p/language-detection/

###### Fabian Kessler

* Forked to https://github.com/optimaize/language-detector from Francois on 2014-02-27
* Rewrote most of the code
* Added JavaDoc
* See changes above, or check the GitHub commit history

###### Francois ROLAND

* Forked to https://code.google.com/p/lang-guess/ from Shuyo's original project.
* Maven integration

###### Robert Theis

* Forked to https://github.com/rmtheis/language-detection from Shuyo's original project.
* Added 16 more language profiles
* Features not (yet) integrated here:
  * profiles stored as Java code
  * Maven multi-module project to reduce size for Android apps


## For Maven Users

The project is in Maven central http://search.maven.org/#artifactdetails%7Ccom.optimaize.languagedetector%7Clanguage-detector%7C0.4%7Cjar this is the latest version:

    <dependency>
        <groupId>com.optimaize.languagedetector</groupId>
        <artifactId>language-detector</artifactId>
        <version>0.5</version>
    </dependency>
