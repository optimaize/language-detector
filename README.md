# language-detector

Language Detection Library for Java

    <dependency>
        <groupId>com.optimaize.languagedetector</groupId>
        <artifactId>language-detector</artifactId>
        <version>0.6</version>
    </dependency>


## Language Support

### 73 Built-in Language Profiles

1. af Afrikaans
2. an Aragonese
3. ar Arabic
4. ast Asturian
5. be Belarusian
6. br Breton
7. ca Catalan
8. bg Bulgarian
9. bn Bengali
10. cs Czech
11. cy Welsh
12. da Danish
13. de German
14. el Greek
15. en English
16. es Spanish
17. et Estonian
18. eu Basque
19. fa Persian
20. fi Finnish
21. fr French
22. ga Irish
23. gl Galician
24. gu Gujarati
25. he Hebrew
26. hi Hindi
27. hr Croatian
28. ht Haitian
29. hu Hungarian
30. id Indonesian
31. is Icelandic
32. it Italian
33. ja Japanese
34. km Khmer
35. kn Kannada
36. ko Korean
37. lt Lithuanian
38. lv Latvian
39. mk Macedonian
40. ml Malayalam
41. mr Marathi
42. ms Malay
43. mt Maltese
44. ne Nepali
45. nl Dutch
46. no Norwegian
47. oc Occitan
48. pa Punjabi
49. pl Polish
50. pt Portuguese
51. ro Romanian
52. ru Russian
53. sk Slovak
54. sl Slovene
55. so Somali
56. sq Albanian
57. sr Serbian
58. sv Swedish
59. sw Swahili
60. ta Tamil
61. te Telugu
62. th Thai
63. tl Tagalog
64. tig Tigre
65. ti Tigrinya
66. tr Turkish
67. uk Ukrainian
68. ur Urdu
69. vi Vietnamese
70. wa Walloon
71. yi Yiddish
72. zh-cn Simplified Chinese
73. zh-tw Traditional Chinese

User danielnaber has made available a profile for Esperanto on his website, see open tasks.

There are two kinds of profiles. The standard ones created from Wikipedia articles and similar.
And the "short text" profiles created from Twitter tweets. Fewer language profiles exist for the
short text, more would be available, see https://github.com/optimaize/language-detector/issues/57

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

See https://github.com/optimaize/language-detector/wiki/Creating-Language-Profiles


## How You Can Help

If your language is not supported yet, then you can provide clean "training text", that is, common text written in your
language. The text should be fairly long (a couple of pages at the very least). If you can provide that, please open
a ticket.

If your language is supported already, but not identified clearly all the time, you can still provide such training
text. We might then be able to improve detection for your language.

If you're a programmer, dig in the source and see what you can improve. Check the open tasks.


## Memory Consumption

Loading all 71 language profiles uses 74MB ram to store the data in memory.
For memory considerations see https://github.com/optimaize/language-detector/wiki/Memory-Consumption


## History and Changes

This project is a fork of a fork, the original author is Nakatani Shuyo.
For detail see https://github.com/optimaize/language-detector/wiki/History-and-Changes


## Where it's used

An adapted version of this is used by the http://www.NameAPI.org server.

https://www.languagetool.org/ is a proof-reading software for LibreOffice/OpenOffice, for the Desktop and for Firefox.



## License

Apache 2 (business friendly)



## Authors

Nakatani Shuyo, Fabian Kessler, Francois ROLAND, Robert Theis

For detail see https://github.com/optimaize/language-detector/wiki/Authors


## For Maven Users

The project is in Maven central http://search.maven.org/#artifactdetails%7Ccom.optimaize.languagedetector%7Clanguage-detector%7C0.4%7Cjar this is the latest version:

    <dependency>
        <groupId>com.optimaize.languagedetector</groupId>
        <artifactId>language-detector</artifactId>
        <version>0.6</version>
    </dependency>
