package socketed.common.config;

import com.google.common.io.Files;
import com.google.gson.*;
import org.apache.logging.log4j.Level;
import socketed.Socketed;
import socketed.common.data.GemType;
import socketed.common.data.RecipientGroup;
import socketed.common.data.entry.effect.*;
import socketed.common.data.entry.effect.activatable.IActivationType;
import socketed.common.data.entry.effect.activatable.PotionGemEffect;
import socketed.common.data.entry.effect.activatable.EnumActivationTypes;
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
    private static final Map<String, GemType> gemEffectsDataMap = new HashMap<>();

    private static File recipientsFolder;
    private static File gemEffectsFolder;

    public static final Map<String, Class<? extends FilterEntry>> filterDeserializerMap = new HashMap<>();
    public static final Map<String, Class<? extends GenericGemEffect>> gemEffectDeserializerMap = new HashMap<>();
    public static final Map<String, Class<? extends IActivationType>> activationDeserializerMap = new HashMap<>();

    static {
        filterDeserializerMap.put(ItemEntry.FILTER_NAME, ItemEntry.class);
        filterDeserializerMap.put(OreEntry.FILTER_NAME, OreEntry.class);
        gemEffectDeserializerMap.put(AttributeGemEffect.FILTER_NAME, AttributeGemEffect.class);
        gemEffectDeserializerMap.put(PotionGemEffect.FILTER_NAME, PotionGemEffect.class);
        activationDeserializerMap.put(Socketed.MODID, EnumActivationTypes.class);
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
        gemEffectsFolder = new File(modFolder, "effects");
        if(!recipientsFolder.exists() || !recipientsFolder.isDirectory()) {
            if(!recipientsFolder.mkdir()) {
                Socketed.LOGGER.log(Level.ERROR, "Could not create recipients configuration folder");
            }
        }
        if(!gemEffectsFolder.exists() || !gemEffectsFolder.isDirectory()) {
            if(!gemEffectsFolder.mkdir()) {
                Socketed.LOGGER.log(Level.ERROR, "Could not create effects configuration folder");
            }
        }
    }

    public static void postInit(){
        loadRecipientData();
        loadGemEffectData();
    }

    public static Map<String, RecipientGroup> getRecipientData() {
        return recipientDataMap;
    }

    public static Map<String, GemType> getEffectData() {
        return gemEffectsDataMap;
    }

    public static void refreshAllData() {
        Socketed.LOGGER.log(Level.INFO, "Clearing existing Socketed data");
        recipientDataMap.clear();
        gemEffectsDataMap.clear();
        Socketed.LOGGER.log(Level.INFO, "Loading new Socketed data");
        loadRecipientData();
        loadGemEffectData();
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

    private static void initDefaultGemEffects() {
        Socketed.LOGGER.log(Level.INFO, "Initializing default Socketed gem effect configs");
        Map<String, GemType> defaultData = DefaultCustomConfig.getDefaultGemEffects();
        for(Map.Entry<String, GemType> entry : defaultData.entrySet()) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement elem = gson.toJsonTree(entry.getValue());
                String entryString = gson.toJson(elem);
                File file = new File(gemEffectsFolder, String.format("%s.json", entry.getKey()));
                if(!file.createNewFile()) Socketed.LOGGER.log(Level.ERROR, "Failed to create new gem effect file, " + entry.getKey());
                else if(!file.setWritable(true)) Socketed.LOGGER.log(Level.ERROR, "Failed to set new gem effect file writeable, " + entry.getKey());
                else {
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(entryString);
                    writer.flush();
                    writer.close();
                }
            }
            catch(Exception e) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to generate default gem effect file, " + entry.getKey() + ", " + e);
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

    private static void loadGemEffectData() {
        Socketed.LOGGER.log(Level.INFO, "=== Starting Socketed Gem Effect Data Loading ===");

        if(gemEffectsFolder == null || !gemEffectsFolder.exists() || !gemEffectsFolder.isDirectory()) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load gem effect config, folder does not exist");
            Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Gem Effect Data Loading ===");
            return;
        }

        if(recipientDataMap.isEmpty()) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load gem effect config, recipient data does not exist");
            Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Gem Effect Data Loading ===");
            return;
        }

        try {
            FilterDeserializer filterDeserializer = new FilterDeserializer();
            for(Map.Entry<String, Class<? extends FilterEntry>> entry : filterDeserializerMap.entrySet()) {
                filterDeserializer.registerType(entry.getKey(), entry.getValue());
            }
            EffectDeserializer effectDeserializer = new EffectDeserializer();
            for(Map.Entry<String, Class<? extends GenericGemEffect>> entry : gemEffectDeserializerMap.entrySet()) {
                effectDeserializer.registerType(entry.getKey(), entry.getValue());
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(FilterEntry.class, filterDeserializer)
                    .registerTypeAdapter(GenericGemEffect.class, effectDeserializer)
                    .create();

            File[] files = gemEffectsFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load gem effect config, folder is invalid");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Gem Effect Data Loading ===");
                return;
            }
            if(files.length <= 0) initDefaultGemEffects();
            files = gemEffectsFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load gem effect config, folder is invalid");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Gem Effect Data Loading ===");
                return;
            }
            if(files.length <= 0) {
                Socketed.LOGGER.log(Level.ERROR, "Failed to load gem effect config, folder is empty");
                Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Gem Effect Data Loading ===");
                return;
            }
            for(File file : files) {
                if(file.isDirectory()) continue;
                JsonElement elem = getJson(file);
                try {
                    GemType gemEffectData = gson.fromJson(elem, GemType.class);
                    if(gemEffectData == null) Socketed.LOGGER.log(Level.WARN, "Failed to load gem effect config file, invalid file: " + file.getName());
                    else {
                        if(gemEffectData.isValid()) gemEffectsDataMap.put(gemEffectData.getName(), gemEffectData);
                        else Socketed.LOGGER.log(Level.WARN, "Failed to load gem effect config file, validation failed: " + file.getName());
                    }
                }
                catch(Exception e) {
                    Socketed.LOGGER.log(Level.WARN, "Failed to load gem effect config file: " + file.getName() + ", " + e);
                }
            }
        }
        catch(Exception e) {
            Socketed.LOGGER.log(Level.ERROR, "Failed to load gem effect config: " + e);
        }

        Socketed.LOGGER.log(Level.INFO, "=== Finishing Socketed Gem Effect Data Loading ===");
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