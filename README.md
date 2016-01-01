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

# Usage
* A, D keys to apply RCS thrusters and rotate
* comma and period to decrease/increase thrust in DPS (Descent Propulsion System)

To change the physic properties edit `org.systemexception.lunarlander.constants.Dimensions`.

# Credits

* [libGDX](https://github.com/libGDX/libGDX): cross-platform Java game development framework

# Known Issues
* In the real LM the thrust was limited to 65% (approx.), we're not applying same limitations.