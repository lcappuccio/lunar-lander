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

# Installation

Download [LWJGL 2.9.4](http://legacy.lwjgl.org/) and copy to a library folder inside the project.
The JARs need to be included in the classpath and to launch the program append `-Djava.library.path="native"` as JVM
parameter.

# Credits

* [jbox2d](https://github.com/jbox2d/jbox2d) for the physics engine
* [LWJGL (LightWeight Java Game Library)](http://legacy.lwjgl.org/) for the game engine