# Table of contents
* [JediTermFX](#jeditermfx)
* [Demo](#demo)
* [Features](#features)
* [Terminal Comparison](#comparison)
* [Code building](#code-building)
* [Running the Application](#application)
    * [Using Maven](#application-maven)
    * [Using Distro](#application-distro)
* [License](#license)
* [Feedback](#feedback)

# JediTermFX <a name="jeditermfx"></a>

JediTermFX is a Terminal Emulator for JavaFX. The project is a result of porting the
[JediTerm](https://github.com/JetBrains/jediterm) project (commit 8366f2b) from Swing to JavaFX. JediTermFX exclusively
utilizes JavaFX components. As a result, the Terminal Emulator based on this library can be seamlessly integrated into
any JavaFX application. A detailed comparison of terminal libraries is provided below.

# Demo <a name="demo"></a>

![JediTermFX demo](./demo.gif)

# Features <a name="features"></a>

* Local terminal for Unix, Mac and Windows using Pty4J
* Xterm emulation - passes most of tests from vttest
* Xterm 256 colours
* Scrolling
* Copy/Paste
* Mouse support
* Terminal resizing from client or server side
* Terminal tabs

# Terminal Comparison <a name="comparison"></a>

Terminal      | JediTermFX  | [JediTerm](https://github.com/JetBrains/jediterm)  | [TerminalFX](https://github.com/javaterminal/TerminalFX) |
:-------------|:----------- |:--------------|:--------------|
GUI Library   | JavaFX      | Swing         | JavaFX        |
Main Component| Canvas      | JComponent    | WebView       |
Languages     | Java        | Java, Kotlin  | Java, JS      |
JPMS Support  | Yes         | No            | Yes           |

# Code Building <a name="code-building"></a>

To build the library use standard Git and Maven commands:

    git clone https://github.com/PavelKastornyy/jeditermfx
    cd jeditermfx
    mvn clean install

# Running the Application <a name="code-building"></a>

The project contains demo application that shows how to use this library. There are two ways to run the application.

## Using Maven <a name="application-maven"></a>

To run application using maven plugin execute the following commands in the root of the project:

    cd jeditermfx-app
    mvn javafx:run

Please note, that debugger settings are in `jeditermfx-app/pom.xml` file.

## Using Distro <a name="application-distro"></a>

After building the project, in the `jeditermfx-app/target` directory, you will find a distribution archive named
`jeditermfx-version-app.tar`. Extracting this file will allow you to launch the application using `.sh` or `.bat`
scripts depending on your operating system.

# License <a name="license"></a>

JediTermFX is dual-licensed under both the LGPLv3 (found in the LICENSE-LGPLv3.txt file in the root directory) and
Apache 2.0 License (found in the LICENSE-APACHE-2.0.txt file in the root directory). You may select, at your option,
one of the above-listed licenses.

# Feedback <a name="feedback"></a>

Any feedback is welcome. Besides, it would be interesting to know for what cases this project is used. It will
help to understand the way the project should go and provide more information in documentation.



