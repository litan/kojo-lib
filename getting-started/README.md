# Getting started with kojo-lib in Scala 3

* Install `scala-cli` from here: https://scala-cli.virtuslab.org/install

* Download one of these files, depending on which language you want:
  - English: https://github.com/litan/kojo-lib/releases/download/v0.3.1/kojo-english.scala
  - Swedish: https://github.com/litan/kojo-lib/releases/download/v0.3.1/kojo-swedish.scala

* Start the Scala 3 REPL with this command in the terminal in the same dir as the above file:
  ```
  scala-cli repl .
  ```

* You should eventually see something similar to:
  ```
  Welcome to Scala 3.1.2 (17.0.2, Java OpenJDK 64-Bit Server VM).
  Type in expressions for evaluation. Or try :help.
           
  scala> 

  ``` 

* Test Kojo with this command inside the REPL after the `scala>` prompt:

  ```
  forward()
  ```

* You should now see the Kojo turtle in a new window drawing a red line.
