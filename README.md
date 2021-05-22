# Kojo Lib

Kojo-Lib provides [Kojo](www.kojo.in) (the Scala based learning environment) as a library that you can use with:
- Scala 2.13.x or Scala 3, and any Scala IDE (Intellij IDEA, Visual Studio Code, etc).
- Any JVM language (that is able to make use of Java jar files).

## Quick Start
- Clone this repo.
- Go into the repo dir in a terminal.
- run `./sbt.sh buildDist`

This will give you two folders of interest:
- `dist` - which contains jars that you can use (on the classpath) with any JVM language.
- `dist-scala` - which contains a couple of Scala jars that you need to include in your classpath for any language other than Scala. 

Enjoy!