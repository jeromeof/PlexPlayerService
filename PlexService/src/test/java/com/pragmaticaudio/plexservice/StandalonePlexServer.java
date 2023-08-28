package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlexPinInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexServerInfo;

import org.yaml.snakeyaml.Yaml;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.yaml.snakeyaml.constructor.Constructor;
import java.nio.file.Paths;
import java.util.Map;

import androidx.annotation.Nullable;

import static com.pragmaticaudio.plexservice.PlexPlayerServer.DEFAULT_PORT;

/**
 * Move this into separate module eventually
 */
public class StandalonePlexServer {

    // The actual PlexPlayerServer implementing the required rest endpoints
    private PlexPlayerServer plexPlayerServer = null;

    private PlexGDM plexGDM = new PlexGDM();

    private Map<String, Object> settings = null;


    List<PlexServerInfo> listServers = new LinkedList<>();  // Placeholders for a list of Plex Servers on local network

    public StandalonePlexServer() {
        settings = loadSettings();
    }

    public void startServer() throws IOException, InterruptedException {
        Object token = getPlexToken(settings);
        if (token == null) {
            System.out.println("No token - please run again and link your account");
            System.exit(1);
        }
        if (settings == null) {
            return;     // No settings no fun
        }
        String ourIpAddress = (String) settings.get("ourIpAddress");
        if (ourIpAddress == null) {
            ourIpAddress = Utils.getIPAddress(true);
            settings.put("ourIpAddress", ourIpAddress);
        }
        plexPlayerServer = new PlexPlayerServer( settings , new ConsoleLogger(), new MockMediaPlayer());

        plexPlayerServer.start();

        listServers = plexGDM.searchForPlexServers(listServers);

        declareOurAvailability();
    }

    private void declareOurAvailability() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        plexGDM.declareAvailability(new PlexPlayerInfo(settings));

                        Thread.sleep(5000); // Sleep for 5 seconds before trying again

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }).start();
        ;
    }

    /**
     * GEt Plex Token - either from ~/.plexPlayer.yml or test the process to
     * generate one and ask user to save it in the ~/.plexPlayer.yml for runs on this
     * simple standalone server
     *
     * @param settings
     * @return
     */
    @Nullable
    private static Object getPlexToken(Map<String, Object> settings) throws IOException, InterruptedException {
        // We need these as the minimal settings to start
        String token = null;
        String uniqueResourceIdenfierForPlayer = null;

        // If we have some settings lets try and load from them
        if (settings != null) {
            token = (String) settings.get("token");
            Map<String, Object> playerInfoMap = (Map<String, Object>) settings.get("playerInfo");
            if (playerInfoMap != null) {
                Object resourceIdenfier = playerInfoMap.get("resourceIdenfiier");
                if (resourceIdenfier != null) {
                    uniqueResourceIdenfierForPlayer = (String) resourceIdenfier;
                }
            }
        }
        PlexCloudService plexCloudService = new PlexCloudService();

        // Still no token - lets ask the user to link
        if (token == null) {
            System.out.println("We need a Plex Token .. Lets generate one");

        // Check for a UUID / resource Idenfiier in the file - in Android this should be consistent
            if (uniqueResourceIdenfierForPlayer == null) {
                UUID uuid = UUID.randomUUID();
                uniqueResourceIdenfierForPlayer = uuid.toString();
                if (settings != null) {
                    Map<String, Object> playerInfoMap = (Map<String, Object>) settings.get("playerInfo");
                    playerInfoMap.put("resourceIdentifier", uniqueResourceIdenfierForPlayer);
                }
            }

            PlexPlayerInfo playerInfo = null;
            if (settings == null) {
                playerInfo = new PlexPlayerInfo("Standalone", "PlexPlayer", DEFAULT_PORT, uniqueResourceIdenfierForPlayer);
            } else {
                playerInfo = new PlexPlayerInfo(settings);
            }
            PlexPinInfo pin = plexCloudService.getPin(playerInfo);
            System.out.println("OK - here is your PIN " + pin.getCode() + "  - verify it by clicking here https://www.plex.tv/link/?pin=" + pin.getCode());
            System.out.println("Please verify in 30 seconds .....");
            PlexPinInfo plexPinInfo = null;
            for (int i=0; i < 30; i++) {
                Thread.sleep(1000); // Sleep for 1 second then check again
                plexPinInfo = plexCloudService.checkPin(pin.getId(), playerInfo);
                if (plexPinInfo.getAuthToken().length() > 0) {
                    break;
                }
            }

            token = plexPinInfo.getAuthToken();
            if (token == null || token.length() == 0) {
                System.out.println("No token - please try again");
                System.exit(-1);
            }
            System.out.println("Your Auth Token is: " + token);
            System.out.println("Create a ~/.plexPlayer.yml which looks like this:\n\n");
            if (uniqueResourceIdenfierForPlayer != null) {
                System.out.println("token: " + token);
                System.out.println("playerInfo:\n" +
                        "   name: Standalone\n" +
                        "   product: PlexPlayer\n" +
                        "   port: 32500");
                System.out.println("   resourceIdentifier: " + uniqueResourceIdenfierForPlayer + "\n\n");
            }
        }
        return token;
    }

    public static Map<String, Object> loadSettings() {
        String homeDirectory = System.getProperty("user.home");
        String yamlFilePath = Paths.get(homeDirectory, ".plexPlayer.yml").toString();

        Yaml yaml = new Yaml(new Constructor(Map.class));

        try (InputStream in = new FileInputStream(yamlFilePath)) {
            return yaml.load(in);
        } catch (Exception e) {
            return null;    // Just handle the missing settings in the caller
        }
    }

    /**
     *  Dumb routine to just keep this standalone server running
     */
    public void waitUntilFinished() {

        System.out.println("PlexPlayer Running ....");

        // Keep the application running indefinitely
        while (true) {
            try {
                Thread.sleep(1000);  // Sleep for 1 second
            } catch (InterruptedException e) {
                // Handle interrupt if necessary
                Thread.currentThread().interrupt();
            }
        }

    }
}
