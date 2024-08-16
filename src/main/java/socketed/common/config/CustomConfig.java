package socketed.common.config;

import com.google.common.io.Files;
import com.google.gson.*;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.data.EffectGroup;
import socketed.common.data.RecipientGroup;
import socketed.common.data.entry.effect.*;
import socketed.common.data.entry.effect.activatable.IActivationType;
import socketed.common.data.entry.effect.activatable.PotionEntry;
import socketed.common.data.entry.effect.activatable.SocketedActivationTypes;
import socketed.common.data.entry.filter.FilterDeserializer;
import socketed.common.data.entry.filter.FilterEntry;
import socketed.common.data.entry.filter.ItemEntry;
import socketed.common.data.entry.filter.OreEntry;

import javax.annotation.Nullable;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.*;

public class CustomConfig {
    private static final Map<String, RecipientGroup> recipientDataMap = new HashMap<>();
    private static final Map<String, EffectGroup> effectDataMap = new HashMap<>();

    private static File recipientsFolder;
    private static File effectsFolder;

    public static final Map<String, Class<? extends FilterEntry>> filterDeserializerMap = new HashMap<>();
    public static final Map<String, Class<? extends EffectEntry>> effectDeserializerMap = new HashMap<>();
    public static final Map<String, Class<? extends IActivationType>> activationDeserializerMap = new HashMap<>();

    static {
        filterDeserializerMap.put(ItemEntry.FILTER_NAME, ItemEntry.class);
        filterDeserializerMap.put(OreEntry.FILTER_NAME, OreEntry.class);
        effectDeserializerMap.put(AttributeEntry.FILTER_NAME, AttributeEntry.class);
        effectDeserializerMap.put(PotionEntry.FILTER_NAME, PotionEntry.class);
        activationDeserializerMap.put(Socketed.MODID, SocketedActivationTypes .class);
        //activationDeserializerMap.put("<ExampleMod.MODID>", ExampleModActivationTypes.class);
    }

    public static void preInit(File file) {
        File modFolder = new File(file, Socketed.MODID);
        if(!modFolder.exists() || !modFolder.isDirectory()) {
            if(!modFolder.mkdir()) {
                Socketed.LOGGER.log(Level.ERROR, "Could not create general configuration folder");
                return;
            }
        }

        recipientsFolder = new File(modFolder, "recipients");
        effectsFolder = new File(modFolder, "effects");
        if(!recipientsFolder.exists() || !recipientsFolder.isDirectory()) {
            if(!recipientsFolder.mkdir()) {
                Socketed.LOGGER.log(Level.ERROR, "Could not create recipients configuration folder");
            }
        }
        if(!effectsFolder.exists() || !effectsFolder.isDirectory()) {
            if(!effectsFolder.mkdir()) {
                Socketed.LOGGER.log(Level.ERROR, "Could not create effects configuration folder");
            }
        }
    }

    public static void postInit(){
        loadRecipientData();
        loadEffectData();
    }

    public static Map<String, RecipientGroup> getRecipientData() {
        return recipientDataMap;
    }

    public static Map<String, EffectGroup> getEffectData() {
        return effectDataMap;
    }

    public static void refreshAllData() {
        Socketed.LOGGER.log(Level.INFO, "Clearing existing Socketed data");
        recipientDataMap.clear();
        effectDataMap.clear();
        Socketed.LOGGER.log(Level.INFO, "Loading new Socketed data");
        loadRecipientData();
        loadEffectData();
    }

    private static void initDefaultRecipients() {
        Socketed.LOGGER.log(Level.INFO, "Initializing default Socketed recipient configs");
        Map<String, RecipientGroup> defaultData = DefaultCustomConfig.getDefaultRecipients();
        for(Map.Entry<String, RecipientGroup> entry : defaultData.entrySet()) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement elem = gson.toJsonTree(entry.getValue());
                String entryString = gson.toJson(elem);
                File file = new File(recipientsFolder, String.format("%s.json", entry.getKey()));
                if(!file.createNewFile()) Socketed.LOGGER.log(Level.ERROR, "Failed to create new recipient file, " + entry.getKey());
                else if(!file.setWritable(true)) Socketed.LOGGER.log(Level.ERROR, "Failed to set new recipient file writeable, " + entry.getKey());
                else {
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(entryString);
                    writer.flush();
                    writer.close();
                }
            }
            catch(Exception e) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to generate default recipient file, " + entry.getKey() + ", " + e);
            }
        }
    }

    private static void initDefaultEffects() {
        Socketed.LOGGER.log(Level.INFO, "Initializing default Socketed effect configs");
        Map<String, EffectGroup> defaultData = DefaultCustomConfig.getDefaultEffects();
        for(Map.Entry<String, EffectGroup> entry : defaultData.entrySet()) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement elem = gson.toJsonTree(entry.getValue());
                String entryString = gson.toJson(elem);
                File file = new File(effectsFolder, String.format("%s.json", entry.getKey()));
                if(!file.createNewFile()) Socketed.LOGGER.log(Level.ERROR, "Failed to create new effect file, " + entry.getKey());
                else if(!file.setWritable(true)) Socketed.LOGGER.log(Level.ERROR, "Failed to set new effect file writeable, " + entry.getKey());
                else {
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(entryString);
                    writer.flush();
                    writer.close();
                }
            }
            catch(Exception e) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to generate default effect file, " + entry.getKey() + ", " + e);
            }
        }
    }

    private static void loadRecipientData() {
        Socketed.LOGGER.log(Level.INFO, "=== Starting Socketed Recipient Data Loading ===");

        if(recipientsFolder == null || !recipientsFolder.exists() || !recipientsFolder.isDirectory()) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load recipient config, folder does not exist");
            Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Recipient Data Loading ===");
            return;
        }

        try {
            FilterDeserializer filterDeserializer = new FilterDeserializer();
            for(Map.Entry<String, Class<? extends FilterEntry>> entry : filterDeserializerMap.entrySet()) {
                filterDeserializer.registerType(entry.getKey(), entry.getValue());
            }
            Gson gson = new GsonBuilder().registerTypeAdapter(FilterEntry.class, filterDeserializer).create();

            File[] files = recipientsFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load recipient config, folder is invalid");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Recipient Data Loading ===");
                return;
            }
            if(files.length <= 0) initDefaultRecipients();
            files = recipientsFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load recipient config, folder is invalid");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Recipient Data Loading ===");
                return;
            }
            if(files.length <= 0) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load recipient config, folder is empty");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Recipient Data Loading ===");
                return;
            }
            for(File file : files) {
                if(file.isDirectory()) continue;
                JsonElement elem = getJson(file);
                try {
                    RecipientGroup data = gson.fromJson(elem, RecipientGroup.class);
                    if(data == null) Socketed.LOGGER.log(Level.WARN, "Failed to load recipient config file, invalid file: " + file.getName());
                    else {
                        if(data.isValid()) recipientDataMap.put(data.getName(), data);
                        else Socketed.LOGGER.log(Level.WARN, "Failed to load recipient config file, validation failed: " + file.getName());
                    }
                }
                catch(Exception e) {
                    Socketed.LOGGER.log(Level.WARN, "Failed to load recipient config file: " + file.getName() + ", " + e);
                }
            }
        }
        catch(Exception e) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load recipient config: " + e);
        }

        Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Recipient Data Loading ===");
    }

    private static void loadEffectData() {
        Socketed.LOGGER.log(Level.INFO, "=== Starting Socketed Effect Data Loading ===");

        if(effectsFolder == null || !effectsFolder.exists() || !effectsFolder.isDirectory()) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load effect config, folder does not exist");
            Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Effect Data Loading ===");
            return;
        }

        if(recipientDataMap.isEmpty()) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load effect config, recipient data does not exist");
            Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Effect Data Loading ===");
            return;
        }

        try {
            FilterDeserializer filterDeserializer = new FilterDeserializer();
            for(Map.Entry<String, Class<? extends FilterEntry>> entry : filterDeserializerMap.entrySet()) {
                filterDeserializer.registerType(entry.getKey(), entry.getValue());
            }
            EffectDeserializer effectDeserializer = new EffectDeserializer();
            for(Map.Entry<String, Class<? extends EffectEntry>> entry : effectDeserializerMap.entrySet()) {
                effectDeserializer.registerType(entry.getKey(), entry.getValue());
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(FilterEntry.class, filterDeserializer)
                    .registerTypeAdapter(EffectEntry.class, effectDeserializer)
                    .create();

            File[] files = effectsFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load effect config, folder is invalid");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Effect Data Loading ===");
                return;
            }
            if(files.length <= 0) initDefaultEffects();
            files = effectsFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load effect config, folder is invalid");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Effect Data Loading ===");
                return;
            }
            if(files.length <= 0) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load effect config, folder is empty");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Effect Data Loading ===");
                return;
            }
            for(File file : files) {
                if(file.isDirectory()) continue;
                JsonElement elem = getJson(file);
                try {
                    EffectGroup effectData = gson.fromJson(elem, EffectGroup.class);
                    if(effectData == null) Socketed.LOGGER.log(Level.WARN, "Failed to load effect config file, invalid file: " + file.getName());
                    else {
                        if(effectData.isValid()) effectDataMap.put(effectData.getName(), effectData);
                        else Socketed.LOGGER.log(Level.WARN, "Failed to load effect config file, validation failed: " + file.getName());
                    }
                }
                catch(Exception e) {
                    Socketed.LOGGER.log(Level.WARN, "Failed to load effect config file: " + file.getName() + ", " + e);
                }
            }
        }
        catch(Exception e) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load effect config: " + e);
        }

        Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Effect Data Loading ===");
    }

    @Nullable
    @SuppressWarnings("UnstableApiUsage")
    private static JsonElement getJson(File file) {
        if(file == null || !file.exists()) {
            Socketed.LOGGER.log(Level.WARN, "Failed to load socketed config file, file does not exist");
            return null;
        }

        try {
            if(!file.setReadable(true)) {
                Socketed.LOGGER.log(Level.WARN, "Failed to load socketed config file, no permission to read the file: " + file.getName());
                return null;
            }
            String fileString = Files.toString(file, Charset.defaultCharset());
            return new JsonParser().parse(fileString);
        }
        catch(Exception e) {
            Socketed.LOGGER.log(Level.WARN, "Failed to load socketed config file: " + file.getName() + ", " + e);
        }
        return null;
    }
}