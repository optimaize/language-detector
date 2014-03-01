# language-detector

Language Detection Library for Java


## Fork

This is a fork from https://code.google.com/p/lang-guess/ (forked on 2014-02-27)
which itself is a fork of the original project https://code.google.com/p/language-detection/

#### Why so much forking?

The original project hasn't seen any commit in a long time. The issue list is growing.
The news page says for 2012 that it now has Maven support, but there is no pom in git.
There is a release in Maven see http://mvnrepository.com/artifact/com.cybozu.labs/langdetect/1.1-20120112
for version 1.1-20120112 but not in git. So I don't know what's going on here.

The lang-guess fork saw quite some commits in 2011 and up to march 2012, then nothing anymore.
It uses Maven.

The 2 projects are not in sync, it looks like they did not integrate changes from each other anymore.

Both are on Google Code, I feel that GitHub is a much better place for contributing.

My goals are to bring the code up to current standards, and to update it for Java 7. So I'll touch a
lot. And with the status of the 2 other projects I fear that my efforts would be a waste of time.

This fork will probably die also. At least it offers an updated code base for those interested.


#### My changes

* Updated all Maven dependency versions
* Replaced last lib dependency with Maven (jsonic)
* Updated to use Java 7 for compilation, and for syntax. It's 2014, and 7 is the only officially supported version by Oracle.
* Code quality improvements
  * Returning interfaces instead of implementations (List instead of ArrayList etc)
  * String .equals instead of ==
  * Replaced StringBuffer with StringBuilder
  * Renamed classes for clarity
  * Made classes immutable, and thus thread safe
  * Made fields private, using accessors
  * Clear null reference concept: using IntelliJ's @Nullable and @NotNull annotations
  * Added JavaDoc, fixed typos
  * Added interfaces
* Made results for short text consistent, no random n-gram selection for short text (configurable).
* Configurable when to remove ASCII. Old code did it when ascii was less than 1/3 of the content.


#### TODO

* Using a logger instead of System.out.println
* Remove old code
* Simplify loading/storing of profiles, creation of profiles. Update to new profile class.
* Cleaning of ASCII must be replaced with removing of minority script content.