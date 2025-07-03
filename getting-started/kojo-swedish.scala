//> using scala 3
//> using dep "net.kogics:kojo-lib:0.3.1,url=https://github.com/litan/kojo-lib/releases/download/v0.3.1/kojo-lib-assembly-0.3.1.jar"

/* The above directives enable scala repl to use Kojo in Scala 3 with this command:
scala repl .
Install scala using the official installer from
https://www.scala-lang.org/download/
*/

// The lines below make Swedish and English commands available on top level:

export net.kogics.kojo.Swedish.*, padda.*, CanvasAPI.*, TurtleAPI.*
export java.awt.Color