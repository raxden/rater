Android Rater
==========

Android Rater is a library that prompts the user to rate the application if application has been launched 7 times and it has been 3 days since the first launch by default. Works on Android 4.1 (API level 16) and upwards.

[![Release](https://img.shields.io/github/tag/raxden/AndroidRater.svg?label=maven central)](https://jitpack.io/#raxden/AndroidRater/) 
[![API](https://img.shields.io/badge/API-16%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=16)

## Usage

In order to use the library, there are 3 options:

**1. Gradle dependency**

 - 	Add the following to your `build.gradle`:
 ```gradle
repositories {
	    maven { url "https://jitpack.io" }
}

dependencies {
	    compile 'com.github.raxden:AndroidRater:3.0.4@aar'
}
```

**2. Maven**
- Add the following to your `pom.xml`:
 ```xml
<repository>
       	<id>jitpack.io</id>
	    <url>https://jitpack.io</url>
</repository>

<dependency>
	    <groupId>com.github.raxden</groupId>
	    <artifactId>AndroidRater</artifactId>
	    <version>3.0.4</version>
</dependency>
```

**3. clone whole repository**
 - Open your **commandline-input** and navigate to your desired destination folder (where you want to put the library)
 - Use the command `git clone https://github.com/raxden/AndroidRater.git` to download the full AndroidRater repository to your computer (this includes the folder of the library project as well as the example project)

### Documentation 

For a **detailed documentation**, please have a look at the [**Wiki**](https://github.com/raxden/AndroidRater/wiki) or the [**Javadocs**](https://jitpack.io/com/github/raxden/AndroidRater/3.0.4/javadoc/).

## LICENSE

    Copyright 2015 Ángel Gómez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.