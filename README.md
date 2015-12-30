# Lunar Lander
Lunar Lander, a [classic 1979 game](https://en.wikipedia.org/wiki/Lunar_Lander_%281979_video_game%29).

**Master**

[![Build Status](https://travis-ci.org/lcappuccio/lunar-lander.svg?branch=master)](https://travis-ci.org/lcappuccio/lunar-lander)
[![codecov.io](https://codecov.io/github/lcappuccio/lunar-lander/coverage.svg?branch=master)](https://codecov.io/github/lcappuccio/lunar-lander?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/62bb1aa717a744fd86c01887c79eadc5)](https://www.codacy.com/app/leo_4/lunar-lander)
[![GitHub license](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://raw.githubusercontent.com/lcappuccio/lunar-lander/master/LICENSE)

**Develop**

[![Build Status](https://travis-ci.org/lcappuccio/lunar-lander.svg?branch=develop)](https://travis-ci.org/lcappuccio/lunar-lander)
[![codecov.io](https://codecov.io/github/lcappuccio/lunar-lander/coverage.svg?branch=develop)](https://codecov.io/github/lcappuccio/lunar-lander?branch=develop)

# Compiling and Running

## Compile

Download [LWJGL 2.9.3](http://legacy.lwjgl.org/) and copy to a library folder inside the project.

`mvn clean compile assembly:single`

## Install

After compilation the runnable jar will be in `$project_home/target` along with the natives folder.

`java -jar lunar-lander_$VERSION.jar -Djava.library.path="natives/"`

## IntelliJ, Netbeans, Eclipse

To run the main class append `-Djava.library.path="natives/"` as JVM parameter.


**NOTE**
The native libs should be the same version in the POM (2.9.3), the binaries are included in the project however.

# Usage
Use W, A, D keys to rotate and apply thrust.

To change the physic properties edit `org.systemexception.lunarlander.constants.GamePhysics`.

# Credits

* [jbox2d](https://github.com/jbox2d/jbox2d) for the physics engine, [JavaDoc](https://github.com/TrentCoder/JBox2D_JavaDoc)
* [LWJGL (LightWeight Java Game Library)](http://legacy.lwjgl.org/) for the game engine
* [Oskar Veerhoek](https://twitter.com/CodingUniverse) for some very good tutorials to quickstart

# Known Issues