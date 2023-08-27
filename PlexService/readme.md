# Plex player Service - Introduction
The Play Player Service is what I believe is required technically to
support a minimal viable plex  'endpoint'  player on Android.Effectively
allowed an android device to be cast a music stream from a plex or
plexamp client controller running
on an end users phone.

This capability is especially useful for Network
streaming products built using Android where the user might want use plex
on their mobile phones to control and 'cast' their plex playlists and music
to their network streamer maybe on the other side of the room.

This is similar in use-case to Roon endpoint or Spotify connect.

The initial version will implement just the timeline and playback capabilties.

I may add the playqueues capabilities typical of a 'full' plex / plexamp
player later

## Plex player Service - technical details
This module implements the plex 'player' protocol as outlined originally  
on this github page: [Plex Media Player Wiki](https://github.com/plexinc/plex-media-player/wiki/Remote-control-API)

Effectively the protocol is to support a 3 way coordination between a plex
'controller' usually a mobile phone running a plex 'end user' application
like Plex or PlexAmp and a Plex Server running on the network and
supporting 'cast' capbilties to a different Plex 'Player' on the home
network. Plex's own Raspberry PI based 'headless' PlexAmp is one popular
such player but this code can provide a simple alternative which can be
integrated into another Android App rather than require that a user runs
the plex or PlexAmp on their Android Streamer Device.

But here is my summary of what is required. There are effectively 6
separate components here.
1. Broadcast availability on the local network of the player 'availability'
2. Listen for broadcast packets from any plex client on the local network and response directly to that client
3. Provide support for the user to link this Plex Player app to their plex account as we need a valid plex token
4. Provide a 'resources' endpoint declaring that the player running and providing its capabilities
5. Provide a timeline endpoint keeping the player in sync with the client 'controller' (optionally provide subscribe / unsubscribe)
6. Provide the various 'player' capabilties playMedia, play, pause, stop, skipForward etc

Once the player has been told to playMedia this minimal example application will
start playing the media using standard android api's and then use a very simple
media player screen showing the album art and artist, album track metadata about
the stream being played. The basic controls will be in place to pause, stop and
skip forward / backward in the track and onto the next.

## Overview of the codebase
There are 3 Android modules in this project:
1. PlexPlayerExample - a minimal android App
2. PlexService - The plex player background 'service' (android intent service)
3. RestServer - a modified copy of RestServer by Skornei

### PlexPlayerExample
This codebase is a minimal viable Android App - effectively use to launch
the PlexService 'Service' with the correct permissions but does provide
some examples of how to link a plex account with the application and how
to receive metadata from the PlexService as its playing music.

### PlexService
This is the main logic of this project and it implements the necessary
endpoints and other integrations required to look like a plex player device
on a home network. Since it uses the RestServer each 'endpoint' is
implemented as a Contrller in the contollersfolder.

These Controllers
typically a Get controller which will read http parameters from the request
and use the java Entities to return the appropriate information to the
api call after communicating with the 'Player' which centrally handles
the playback and api interaction with the media playing. The Player stores
a queue and provides the support to Controllers to report the correct
information back to the Plex 'Client'.

The Player also communications via Intents back with the main activity
provide updates to the user.

Some other classes here are as follows:
1. PlexPlayerDaemon - implements the Android Intent Service and kicks off some threads for connectivity
2. PlexPlayerServer - Declares the various controllers and wraps the RestServer
3. PlexPinService - implements the protocol used by Plex to generate a PIN and validate that PIN and get a valid TOKEN
4. PlexGDM - handles the 'Good Day Mate' broadcast protocol Plex uses on a home network
5. PlexAuthenticaiton - current empty as most Plex home network calls uses a TOKEN

Note: To make debuggined easier (as Android simulator can be difficult to configure to receive and broadcast UDP packets)
I make a 'StandalonePlexServer' - ultimately I will move this into a full java application but in Android Studio it needs to
be caled from a 'unittest' to properly get access to Android classes (if it needs them). Therefore I created a
StandalonePlexPlayerTest which calls this 'standard' java codebase within Android Studio.


### Rest Server
This Rest Server code base by Skornei was selected a it provided a nice
annotations and high level abstration over the low level nanohttpd. I
moved the code into the project so I could freely modify it and I have
added a few new features to suit the needs to manipulate the http response
headers. I might add more capabilties as I needed

[Original Codebase here](https://github.com/skornei/restserver)

### Some PLEX Player technical details
In this section I will provide a little details on how the code hooks into plex.
1. PlexGDM.declareAvailability() - This method starts the process by multicasting a UDP packet on 32412. If this packet is scene by a Plex 'client' on the network, that client will reply and then this class will send HELLO message to that 'client'
2. PlexPlayerServer.updatePlexTvConnection() - This method keeps plex up to date with our details. Once every 60seconds this will connect to the plex cloud (using the PlexCloudService) and update our details. Note: This needs a valid PlexToken
3. PlexCloudService.getPin() and PlexCloudService.checkPin() - These methods provide a simple wrapper which use Plex Cloud Service to generate a PinInfo object with a 4 digit code and once the user has typed this code checkPin converts that code into a plexToken.
4. controllers - Each 'endpoint' is implemented via a different controller - mostly these controllers use a common BaseController for helper methods, including if they need access to the Android Application 'context'




