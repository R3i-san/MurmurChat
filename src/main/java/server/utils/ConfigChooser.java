package server.utils;

import server.domain.DtoConfig;
import server.repo.ConfigRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigChooser {

    private int num;
    private List<Config> configs;

    public ConfigChooser(){
        configs = new ArrayList<>();
        pick();
        init();
    }

    private void pick(){
        System.out.print("Numéro d'instance : ");
        Scanner console = new Scanner(System.in);
        int instance = console.nextInt();
        this.num = instance;
    }

    private void init(){
        ConfigRepo repo = new ConfigRepo("/src/main/resources/json/config.json");
        DtoConfig c = repo.getConfigs().get(num-1);

        Config.UNICAST_CLIENT_PORT = c.UNICAST_CLIENT_PORT;
        Config.UNICAST_RELAY_PORT  = c.UNICAST_RELAY_PORT;
        Config.BROADCAST_PORT      = c.BROADCAST_PORT;
        Config.SALT_SIZE           = c.SALT_SIZE;
        Config.BROADCAST_ADDRESS   = c.BROADCAST_ADDRESS;
        Config.DOMAIN_NAME         = c.DOMAIN_NAME;
        Config.AES_KEY             = c.AES_KEY;
        Config.PUBLIC_AES_KEY      = c.PUBLIC_AES_KEY;
        Config.AES_KEY_SIZE        = c.AES_KEY_SIZE;
        Config.GCM_IV_LENGTH       = c.GCM_IV_LENGTH;
        Config.GCM_TAG_LENGTH      = c.GCM_TAG_LENGTH;
        Config.BROADCAST_WAIT_TIME = c.BROADCAST_WAIT_TIME;
        Config.U_REPO_PATH         = c.U_REPO_PATH;
        Config.F_REPO_PATH         = c.F_REPO_PATH;
        Config.T_REPO_PATH         = c.T_REPO_PATH;
        Config.RT_REPO_PATH         = c.RT_REPO_PATH;
    }


}
