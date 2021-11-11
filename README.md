# MyFlightsServer

[![Build Status](https://travis-ci.com/DominikKossinski/MyFlightsServer.svg?token=BD8WsprFssUNh6SkrEvC&branch=master)](https://travis-ci.com/DominikKossinski/MyFlightsServer)

Server for Android application, that allows saving airplanes and airports. Then it is possible to store information
about flights between two airports using selected
airplane. [Android application repository](http://github.com/DominikKossinski/MyFLights)

## Firebase

Environment variables requiered to connect to firebase:

```
 GOOGLE_APPLICATION_CREDENTIALS = <path to admin sdk config file>
```

## Firestorage

```
    FIRESTORAGE_NAME = <Firestorage Name>
    IMAGE_URL = <default url of image uploaded to image storage>
```

## MongoDB

Run MongoDB server:

```
<MongoDB-path>/mongod.exe --dbpath <data path>
```

## Build and Run

Project uses Gradle, command to run server:

```
    ./gradlew bootRun
```
