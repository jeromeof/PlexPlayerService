# PlexPlayerService

Simple Android Studio Project to demonstrate the PlexPlayer Service implementtion which allows any Android Music Player application embed a Plex 'Player' as a background Intent. 
For more details please read the [Technical Overview](PlexService/readme.md)

To run a standalone version - without requiring the android emulator configured for UDP packets.  

Try the following - to build without the tests:
```
gradle build -x test  
```
Then run the tests:
```
gradle test -i   
```

You should hopefully see the following:

```
Successfully started process 'Gradle Test Executor 8'

com.pragmaticaudio.plexservice.StandalonePlexPlayerTest > testServer STANDARD_OUT
    We need a Plex Token .. Lets generate one
    OK - here is your PIN XY11  - verify it by clicking here https://www.plex.tv/link/?pin=XY11
    Please verify in 30 seconds .....
```

This means the instance has not been linked with your plex instance (equivalent code will be needed in the Android Settings 'activity' when running as an android 'service' but in the standalone version I just need a local .yml file).

So open the URL e.g.  https://www.plex.tv/link/?pin=XY11 in the example above.

This should allow you to login to your plex account and link that account with this instance - if you do this within 30 seconds the unit test will continue to
give you details of content requried within the local ~/.plexPlayer.yml that you should create (so you can run multiple times without needing a new token each time).

e.g.
```
Successfully started process 'Gradle Test Executor 8'

com.pragmaticaudio.plexservice.StandalonePlexPlayerTest > testServer STANDARD_OUT
    We need a Plex Token .. Lets generate one
    OK - here is your PIN XY11  - verify it by clicking here https://www.plex.tv/link/?pin=XY11
    Please verify in 30 seconds .....
     Your Auth Token is: <REAL_TOKEN_HERE>
    Create a ~/.plexPlayer.yml which looks like this:


    token: <REAL_TOKEN_HERE>
    playerInfo:
       name: Standalone
       product: PlexPlayer
       port: 32500
       resourceIdentifier: 123213123-234324-234234-234234


    PlexPlayer Running ....

```
Simple copy these details from token: down to resourceIdenfifieir into this yml file in that location and then stop (with Ctrl Break) the unit tests and then restart the unit tests again, i.e.

```
gradle test -i   
```

And this time the standalone version of the application should be running without requiring a new token.