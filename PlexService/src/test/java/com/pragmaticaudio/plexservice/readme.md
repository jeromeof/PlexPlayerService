# Plex Player - Tests
## Standalone Plex Server
This 'text' is really just a bare bones Java command line application which can be used to test all the
network connectivity and calls required to be a PlexPlayer without needing to run the Android Simualtor.

I developed the codebase for the PlexPlayer to at its core be standard Java - therefore it can run without
needing to run the simulator. This was especially important with UDP network calls as it can be difficult
and time consuming to setup the simulator to 'proxy' the network broadcasts from the virtual network
setup for the simulator and the real network with a real Plex server actually running.

The Standard Plex Server needs a .plexPlayer.yml configuration file in the home folder - this folder
takes the place of the Android Settings entries and can be used to 'persist' values like the Plex
token used to connect to Plex once this application has been 'paired' with a Plex Account.

Exmaple of the .plexPlayer.yml
```
token: uS234234324-xx
playerInfo:
  name: Standalone
  product: PlexPlayer
  port: 32501
  resourceIdentifier: 234234324-3242-23423423423432
```