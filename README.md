# language-detector

Language Detection Library for Java



## History and Changes

This is a fork from https://code.google.com/p/lang-guess/ (forked on 2014-02-27) which itself is a fork
of the original project https://code.google.com/p/language-detection/

#### Changes made here

##### Functional Changes

* Made results for short text consistent, no random n-gram selection for short text (configurable).
* Configurable when to remove ASCII (foreign script). Old code did it when ascii was less than 1/3 of the content, and only for ASCII.
* New n-gram generation. Faster, and flexible (filter, space-padding). Previously it was hardcoded to 1, 2 and 3-grams, and it has hardcoded which n-grams were ignored.
* LanguageDetector is now safe to use multi-threaded.
* Clear code to safely load profiles and use them, no state in static fields.
* Easier to generate your own language profiles based on training text, and to load and store them.
* Feature to weight prefix and suffix n-grams higher.

##### Technical Changes

* Updated to use Java 7 for compilation, and for syntax. It's 2014, and 7 is the only officially supported version by Oracle.
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
* Removed the "seed" completely (for the Random() number generator, I don't see the use).
* Updated all Maven dependency versions
* Replaced last lib dependency with Maven (jsonic)

##### TODO

* Using a logger instead of System.out.println
* Remove old code (still a bit left)


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





## Authors

###### Nakatani Shuyo

* Started the project and built most of the functionality. Provided the language profiles.
* Project is at https://code.google.com/p/language-detection/

###### Francois ROLAND

* Forked to https://code.google.com/p/lang-guess/
* Maven integration

###### Fabian Kessler

* Forked to https://github.com/optimaize/language-detector from Francois on 2014-02-27
* Rewrote most of the code
* Added JavaDoc
* See changes above, or check the GitHub commit history


