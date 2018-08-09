# GUI-2048 game

## How to play this game

### Build and run class files

Go to `GUI-2048` folder and run this bash command:

```sh
javac -d build -sourcepath src/ src.com.linhusp.twentyfortyeight.Start.java && cd build && java com.linhusp.twentyfortyeight.Start
```

### Build `JAR` file from source

Go to `PATH/TO/build` folder.

To build `jar` file:

```sh
jar cvfm G2048.jar manifest.txt *.class
```

The `jar` file will be appear in `build` foler.

After the build success you can move this file to anywhere.

To add mod execute to `jar` file (linux only):

```sh
chmod +x G2048.jar
```

To run `jar` file you could open as normal or use:

```sh
java -jar PATH/TO/G2048.jar
```
