package socketed.common.config;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import socketed.Socketed;
import socketed.common.jsondata.GemCombinationType;
import socketed.common.jsondata.GemType;
import socketed.common.jsondata.entry.effect.AttributeGemEffect;
import socketed.common.jsondata.entry.effect.EffectDeserializer;
import socketed.common.jsondata.entry.effect.GenericGemEffect;
import socketed.common.jsondata.entry.effect.activatable.EnumActivationType;
import socketed.common.jsondata.entry.effect.activatable.IActivationType;
import socketed.common.jsondata.entry.effect.activatable.PotionGemEffect;
import socketed.common.jsondata.entry.filter.*;

import javax.annotation.Nullable;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.*;

public class JsonConfig {
    private static final Map<String, GemType> gemTypesDataMap = new HashMap<>();
    private static final Map<String, GemCombinationType> gemCombinationDataMap = new HashMap<>();
    private static List<GemCombinationType> sortedGemCombinationDataList = null;

    private static File gemTypesFolder;
    private static File gemCombinationFolder;

    public static final Map<String, Class<? extends FilterEntry>> filterDeserializerMap = new HashMap<>();
    public static final Map<String, Class<? extends GenericGemEffect>> gemEffectDeserializerMap = new HashMap<>();
    public static final Map<String, Class<? extends IActivationType>> activationDeserializerMap = new HashMap<>();

    static {
        filterDeserializerMap.put(BlockAllFilterEntry.FILTER_NAME, BlockAllFilterEntry.class);
        filterDeserializerMap.put(ItemEntry.FILTER_NAME, ItemEntry.class);
        filterDeserializerMap.put(OreEntry.FILTER_NAME, OreEntry.class);
        gemEffectDeserializerMap.put(AttributeGemEffect.FILTER_NAME, AttributeGemEffect.class);
        gemEffectDeserializerMap.put(PotionGemEffect.FILTER_NAME, PotionGemEffect.class);
        activationDeserializerMap.put(Socketed.MODID, EnumActivationType.class);
        //activationDeserializerMap.put("<ExampleMod.MODID>", ExampleModActivationTypes.class);
    }

    public static void preInit(File file) {
        File modFolder = new File(file, Socketed.MODID);
        if(!modFolder.exists() || !modFolder.isDirectory()) {
            if(!modFolder.mkdir()) {
                Socketed.LOGGER.error("Could not create general configuration folder");
                return;
            }
        }

        gemTypesFolder = new File(modFolder, "gemtypes");
        if(!gemTypesFolder.exists() || !gemTypesFolder.isDirectory()) {
            if(!gemTypesFolder.mkdir()) {
                Socketed.LOGGER.error("Could not create gem configuration folder");
            }
        }

        gemCombinationFolder = new File(modFolder, "gemcombinations");
        if(!gemCombinationFolder.exists() || !gemCombinationFolder.isDirectory()) {
            if(!gemCombinationFolder.mkdir()) {
                Socketed.LOGGER.error("Could not create gem combination configuration folder");
            }
        }
    }

    public static void postInit(){
        loadGemTypeData();
        loadGemCombinationData();
    }

    public static Map<String, GemType> getGemData() {
        return gemTypesDataMap;
    }
    public static Map<String, GemCombinationType> getGemCombinationData() {
        return gemCombinationDataMap;
    }

    public static void refreshAllData() {
        Socketed.LOGGER.info("Clearing existing Socketed jsondata");
        gemTypesDataMap.clear();
        gemCombinationDataMap.clear();
        sortedGemCombinationDataList = null;
        Socketed.LOGGER.info("Loading new Socketed jsondata");
        loadGemTypeData();
        loadGemCombinationData();
    }

    private static void initDefaultGemTypes() {
        Socketed.LOGGER.info("Initializing default Socketed gem type configs");
        Map<String, GemType> defaultData = DefaultJsonConfig.getDefaultGemTypes();
        for(Map.Entry<String, GemType> entry : defaultData.entrySet()) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement elem = gson.toJsonTree(entry.getValue());
                String entryString = gson.toJson(elem);
                File file = new File(gemTypesFolder, String.format("%s.json", entry.getKey()));
                if(!file.createNewFile()) Socketed.LOGGER.error("Failed to create new gem type file, " + entry.getKey());
                else if(!file.setWritable(true)) Socketed.LOGGER.error("Failed to set new gem type file writeable, " + entry.getKey());
                else {
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(entryString);
                    writer.flush();
                    writer.close();
                }
            }
            catch(Exception e) {
                Socketed.LOGGER.error("Failed to generate default gem type file, " + entry.getKey() + ", " + e);
            }
        }
    }

    private static void initDefaultGemCombinations() {
        Socketed.LOGGER.info("Initializing default Socketed gem type configs");
        Map<String, GemCombinationType> defaultCombinationData = DefaultJsonConfig.getDefaultGemCombinations();
        for(Map.Entry<String, GemCombinationType> entry : defaultCombinationData.entrySet()) {
            try {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement elem = gson.toJsonTree(entry.getValue());
                String entryString = gson.toJson(elem);
                File file = new File(gemCombinationFolder, String.format("%s.json", entry.getKey()));
                if(!file.createNewFile()) Socketed.LOGGER.error("Failed to create new gem combination file, " + entry.getKey());
                else if(!file.setWritable(true)) Socketed.LOGGER.error("Failed to set new gem combination file writeable, " + entry.getKey());
                else {
                    PrintWriter writer = new PrintWriter(file);
                    writer.write(entryString);
                    writer.flush();
                    writer.close();
                }
            }
            catch(Exception e) {
                Socketed.LOGGER.error("Failed to generate default gem combination file, " + entry.getKey() + ", " + e);
            }
        }
    }

    private static void loadGemTypeData() {
        Socketed.LOGGER.info("=== Starting Socketed Gem Type Data Loading ===");

        if(gemTypesFolder == null || !gemTypesFolder.exists() || !gemTypesFolder.isDirectory()) {
            Socketed.LOGGER.error("Failed to load gem type config, folder does not exist");
            Socketed.LOGGER.info("=== Finishing Socketed Gem Type Data Loading ===");
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

            File[] files = gemTypesFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.error("Failed to load gem type config, folder is invalid");
                Socketed.LOGGER.info("=== Finishing Socketed Gem Type Data Loading ===");
                return;
            }
            if(files.length <= 0) initDefaultGemTypes();
            files = gemTypesFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.error("Failed to load gem type config, folder is invalid");
                Socketed.LOGGER.info("=== Finishing Socketed Gem Type Data Loading ===");
                return;
            }
            if(files.length <= 0) {
                Socketed.LOGGER.error("Failed to load gem type config, folder is empty");
                Socketed.LOGGER.info("=== Finishing Socketed Gem Type Data Loading ===");
                return;
            }
            for(File file : files) {
                if(file.isDirectory()) continue;
                JsonElement elem = getJson(file);
                try {
                    GemType gemType = gson.fromJson(elem, GemType.class);
                    if(gemType == null) Socketed.LOGGER.warn("Failed to load gem type config file, invalid file: " + file.getName());
                    else {
                        if(gemType.isValid()) gemTypesDataMap.put(gemType.getName(), gemType);
                        else Socketed.LOGGER.warn("Failed to load gem type config file, validation failed: " + file.getName());
                    }
                }
                catch(Exception e) {
                    Socketed.LOGGER.warn("Failed to load gem type config file: " + file.getName() + ", " + e);
                }
            }
        }
        catch(Exception e) {
            Socketed.LOGGER.error("Failed to load gem type config: " + e);
        }

        Socketed.LOGGER.info("=== Finishing Socketed Gem Type Data Loading ===");
    }

    private static void loadGemCombinationData() {
        Socketed.LOGGER.info("=== Starting Socketed Gem Combination Data Loading ===");

        if(gemCombinationFolder == null || !gemCombinationFolder.exists() || !gemCombinationFolder.isDirectory()) {
            Socketed.LOGGER.error("Failed to load gem combination config, folder does not exist");
            Socketed.LOGGER.info("=== Finishing Socketed Gem Combination Data Loading ===");
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

            File[] files = gemCombinationFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.error("Failed to load gem combination config, folder is invalid");
                Socketed.LOGGER.info("=== Finishing Socketed Gem Combination Data Loading ===");
                return;
            }
            if(files.length <= 0) initDefaultGemCombinations();
            files = gemCombinationFolder.listFiles();
            if(files == null) {
                Socketed.LOGGER.error("Failed to load gem combination config, folder is invalid");
                Socketed.LOGGER.info("=== Finishing Socketed Gem Combination Data Loading ===");
                return;
            }
            if(files.length <= 0) {
                Socketed.LOGGER.error("Failed to load gem combination config, folder is empty");
                Socketed.LOGGER.info("=== Finishing Socketed Gem Combination Data Loading ===");
                return;
            }
            for(File file : files) {
                if(file.isDirectory()) continue;
                JsonElement elem = getJson(file);
                try {
                    GemCombinationType gemCombination = gson.fromJson(elem, GemCombinationType.class);
                    if(gemCombination == null) Socketed.LOGGER.warn("Failed to load gem combination config file, invalid file: " + file.getName());
                    else {
                        if(gemCombination.isValid()) gemCombinationDataMap.put(gemCombination.getName(), gemCombination);
                        else Socketed.LOGGER.warn("Failed to load gem combination config file, validation failed: " + file.getName());
                    }
                }
                catch(Exception e) {
                    Socketed.LOGGER.warn("Failed to load gem combination config file: " + file.getName() + ", " + e);
                }
            }
        }
        catch(Exception e) {
            Socketed.LOGGER.error("Failed to load gem combination config: " + e);
        }

        Socketed.LOGGER.info("=== Finishing Socketed Gem Combination Data Loading ===");
    }

    @Nullable
    @SuppressWarnings("UnstableApiUsage")
    private static JsonElement getJson(File file) {
        if(file == null || !file.exists()) {
            Socketed.LOGGER.warn("Failed to load socketed config file, file does not exist");
            return null;
        }

        try {
            if(!file.setReadable(true)) {
                Socketed.LOGGER.warn("Failed to load socketed config file, no permission to read the file: " + file.getName());
                return null;
            }
            String fileString = Files.toString(file, Charset.defaultCharset());
            return new JsonParser().parse(fileString);
        }
        catch(Exception e) {
            Socketed.LOGGER.warn("Failed to load socketed config file: " + file.getName() + ", " + e);
        }
        return null;
    }

    //Order matters bc a list of gems might fit multiple gem combinations. more strict combinations will be found first
    public static List<GemCombinationType> getSortedGemCombinationData() {
        if(sortedGemCombinationDataList == null){
            sortedGemCombinationDataList = new ArrayList<>(gemCombinationDataMap.values());
            sortedGemCombinationDataList.sort((v1,v2) -> Boolean.compare(!v1.getIsStrictOrder(),!v2.getIsStrictOrder()));
            sortedGemCombinationDataList.sort((v1,v2) -> Boolean.compare(!v1.getIsStrictSocketCount(),!v2.getIsStrictSocketCount()));
        }
        return sortedGemCombinationDataList;
    }
}