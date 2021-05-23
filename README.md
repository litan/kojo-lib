# Kojo Lib

Kojo-Lib provides [Kojo](www.kojo.in) (the Scala based learning environment) as a library that you can use with:
- Scala 2.13.x or Scala 3, and any Scala IDE (Intellij IDEA, Visual Studio Code, etc).
- Any JVM language (that is able to make use of Java jar files).

## Quick Start
- Clone this repo.
- Go into the repo dir in a terminal.
- run `./sbt.sh buildDist`.

This will give you two folders of interest:
- `dist` - which contains jars that you can use (on the classpath) with any JVM language.
- `dist-scala` - which contains a couple of Scala jars that you need to include in your classpath for any language other than Scala.

### Minimal sample client programs
- [In Scala](https://github.com/litan/kojo-lib/blob/main/src/main/scala/driver/Main.scala)
- [In Java](https://github.com/litan/kojo-lib/blob/main/src/main/java/driver/Main4Java.java)

### Examples - from the Kojo Showcase menu and more
- [Examples folder in this repo](https://github.com/litan/kojo-lib/tree/main/src/main/scala/example)

## Published artifacts
Coming soon (jars published on maven central).

---

Enjoy!
