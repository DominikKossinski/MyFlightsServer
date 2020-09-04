# MyFlightsServer

[![Build Status](https://travis-ci.com/DominikKossinski/MyFlightsServer.svg?token=BD8WsprFssUNh6SkrEvC&branch=master)](https://travis-ci.com/DominikKossinski/MyFlightsServer)


Environment variables requiered to connect to firebase:
```
 GOOGLE_APPLICATION_CREDENTIALS = <path to admin sdk config file>
```


Environment variables required to connect to admin database:
```
    DB_URL = <url of db>*
    DB_USER = <user of db>
    DB_PASSWORD = <user's password to db> 
```

\* in case of time zone error you need to append this to db url:
```
    ?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false
``` 