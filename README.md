# Kojo Lib

Kojo-Lib provides [Kojo](www.kojo.in) (the Scala based learning environment) as a library that you can use with:
- [Scala 3](https://github.com/litan/kojo-lib-scala3samples) or Scala 2.13.x, and any Scala IDE (Intellij IDEA, Visual Studio Code + Metals, etc).
- Any JVM language (that is able to consume Java jar files).

## Quick Start (for [scala-cli](https://scala-cli.virtuslab.org/install))

* Let scala-cli download kojo-lib for you by using these magic comments starting with `//>` like so in a `.scala`-file:
  ```
  //> using scala "3"
  //> using lib "net.kogics:kojo-lib:0.1.1,url=https://github.com/litan/kojo-lib/releases/download/v0.1.1/kojo-lib-assembly-0.1.1.jar"

  import net.kogics.kojo.English.*, CanvasAPI.*, TurtleAPI.*

  @main def run = 
    forward(200)
    // your turtle graphics code here
  ```

  - Run your program with `scala-cli run .`

  - Start the repl and play with kojo with `scala-cli repl .`
  ```
  scala> import net.kogics.kojo.English.*, CanvasAPI.*, TurtleAPI.*

  scala> forward()
  ```

* Or download the latest assembly jar manually and start the repl using scala-cli:
  ```
  $ wget https://github.com/litan/kojo-lib/releases/download/v0.1.1/kojo-lib-assembly-0.1.1.jar

  $ scala-cli repl . --jar kojo-lib-assembly-0.1.1.jar

  Welcome to Scala 3.1.2 (17.0.2, Java OpenJDK 64-Bit Server VM).
  Type in expressions for evaluation. Or try :help.

  scala> import net.kogics.kojo.English.*, CanvasAPI.*, TurtleAPI.*

  scala> forward()

  ```

You can use other languages by importing one of the available language-specific APIs:
*  Get both the Swedish and English API in scope:
```
import net.kogics.kojo.Swedish.*, padda.*, CanvasAPI.*, TurtleAPI.*
```


## Quick Start (for Scala / sbt)

Add the following dependency in your build.sbt file:

```
val kojoLibVersion = "0.1.0"
libraryDependencies += "net.kogics" % "kojo-lib" % kojoLibVersion from s"https://github.com/litan/kojo-lib/releases/download/v$kojoLibVersion/kojo-lib-assembly-$kojoLibVersion.jar"
```

## Doing a manual build (for any JVM language)
- Clone this repo.
- Go into the repo dir in a terminal.
- run `./sbt.sh buildDist` to get the `dist` and `dist-scala` dirs (explained below).
- run `./sbt.sh assembly` to get a fat kojo-lib-assembly-x.y.z.jar in target/scala-2.13

This will give you two folders of interest:
- `dist` - which contains jars that you can use (on the classpath) with any JVM language. *Note* - you can also use the latest kojo-lib-assembly-x.y.z.jar release jar in place of these jars. 
- `dist-scala` - which contains a couple of Scala jars that you need to include in your classpath for any language other than Scala.

### Minimal sample client programs
- [In Scala 3](https://github.com/litan/kojo-lib-scala3samples/blob/main/src/main/scala/example/Main.scala)
- [In Scala 2.13.x](https://github.com/litan/kojo-lib/blob/main/src/main/scala/driver/Main.scala)
- [In Java](https://github.com/litan/kojo-lib/blob/main/src/main/java/driver/Main4Java.java)

### Examples (mostly from the Kojo Showcase menu)
- [Inner Eye](https://github.com/litan/kojo-lib/blob/main/src/main/scala/example/InnerEye.scala)
- [Mandelbrot Set](https://github.com/litan/kojo-lib/blob/main/src/main/scala/example/MandelbrotSet.scala)
- [Car Ride](https://github.com/litan/kojo-lib/blob/main/src/main/scala/example/CarRide.scala)
- [Platformer Game](https://github.com/litan/kojo-lib/blob/main/src/main/scala/example/DemoPlatformer.scala)
- [Unbeatable Tic Tac Toe](https://github.com/litan/kojo-lib/blob/main/src/main/scala/example/TicTacToeUnbeatable.scala)  
- [Othello](https://github.com/litan/kojo-lib/blob/main/src/main/scala/game/othello/main.scala)
- [And more...](https://github.com/litan/kojo-lib/tree/main/src/main/scala/example)

## Published artifacts
Coming soon (jars published on maven central).

---

Enjoy!
