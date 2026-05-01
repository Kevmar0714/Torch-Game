# Torch Game

Torch Game is a Java tile-based dungeon game with procedural world generation, room and hallway generation, torches, lighting, avatar movement, mouse pathfinding, and save/load support.

## Features

- Procedurally generated rooms and hallways
- Seed-based world generation
- Avatar movement with `W A S D`
- Mouse-click pathfinding movement
- Torch lighting system
- Toggleable lighting
- 3 save slots
- Save worlds using `1 2 3`
- Save and quit with `:Q`

## Requirements

- Java JDK 17 or newer
- Princeton `algs4.jar`

Download `algs4.jar` here:

```text
https://algs4.cs.princeton.edu/code/algs4.jar
```

```bash
mkdir -p out
javac -cp "lib/algs4.jar;src" -d out src/core/*.java src/tileengine/*.java src/utils/*.java
java -cp "lib/algs4.jar;out" core.Main
```

This command format works on Windows/Git Bash.

## macOS/Linux Compile and Run

On macOS or Linux, use `:` instead of `;` in the classpath:

```bash
mkdir -p out
javac -cp "lib/algs4.jar:src" -d out src/core/*.java src/tileengine/*.java src/utils/*.java
java -cp "lib/algs4.jar:out" core.Main
```
