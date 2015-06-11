Android Rater
==========

Android Rater is a library that prompts the user to rate the application if application has been launched 7 times and it has been 3 days since the first launch by default. Works on Android 2.3 (API level 10) and upwards.

[![Release](https://img.shields.io/github/release/raxden/AndroidRater.svg?label=maven central)](https://jitpack.io/#raxden/AndroidRater/) [![API](https://img.shields.io/badge/API-10%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=10)

## Usage

In order to use the library, there are 3 options:

**1. Gradle dependency**

 - 	Add the following to your `build.gradle`:
 ```gradle
repositories {
	    maven { url "https://jitpack.io" }
}

dependencies {
	    compile 'com.github.raxden:AndroidRater:v2.0.1@aar'
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
	    <version>v2.0.1</version>
</dependency>
```

**3. clone whole repository**
 - Open your **commandline-input** and navigate to your desired destination folder (where you want to put the library)
 - Use the command `git clone https://github.com/raxden/AndroidRater.git` to download the full AndroidRater repository to your computer (this includes the folder of the library project as well as the example project)

### Documentation 

For a **detailed documentation**, please have a look at the [**Wiki**](https://github.com/raxden/AndroidRater/wiki).
