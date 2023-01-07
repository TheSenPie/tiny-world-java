# tiny-world-java
Tiny World is a personal custom game engine created with Java and OpenGL for fun.

## Getting Started

To run the project make sure you have have:
* Windows 8+: Windows 7 should work too, but was not tested.
* JDK 8+: You can download Oracle JDK from [their wesbite](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html).

Then download the [game.zip](https://github.com/TheSenPie/tiny-world-java/blob/master/game.zip), extract it and run the game.jar with double-click.

Controls are: WASD and mouse for movement, press T to toggle wireframe mode. 

## Preview
Video [preview](https://youtu.be/hA-29bpJ4Bc).

![java_6YrqPL9KJS](https://user-images.githubusercontent.com/20699484/211156030-a2cfa780-3aa9-44d3-8793-5c02d4987058.jpg)

## Run source code
In case you want to run the project from source code follow these steps.

Prerequisite
* Make sure you have installed [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (the “Community” edition is sufficient)

Then,
1. Clone the repository.
2. Open in IntelliJ IDEA as a project.
3. Make sure Gradle is synced. Firs time it may take several minutes to sync the project.
4. Add new Run/Debug Configuration. 
    * Main class: select am.aua.game.Game as the main class.
    * Working directory: select the 'assets' folder in './src/assets'.
    * Use classpath of the module: tiny-world-java.main
5. Run the created configuration.
