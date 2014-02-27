# language-detector

Language Detection Library for Java


## Fork

This is a fork from https://code.google.com/p/lang-guess/ (forked on 2014-02-27)
which itself is a fork of the original project https://code.google.com/p/language-detection/

#### Why so much forking?

The original project hasn't seen any commit in a long time. The issue list is long.
The news page says for 2012 that it now has Maven support, but there is no maven in git.
There is a release in Maven see http://mvnrepository.com/artifact/com.cybozu.labs/langdetect/1.1-20120112
for version 1.1-20120112 but not in git. So I don't know what's going on here.

The lang-guess fork saw quite some commits in 2011 and up to march 2012, then nothing anymore.
It uses Maven.

The 2 projects are not in sync, it looks like they did not integrate changes from each other anymore.

Both are on google code, I feel that GitHub is a much better place for contributing.

This fork will probably die also. The goal is to publish my changes. I did not want to start off
with a non-maven version. And publishing back to the first fork seemed impractical too. So here it is.

#### My changes

* Updated all Maven dependency versions
* Replaced last lib dependency with Maven (jsonic)