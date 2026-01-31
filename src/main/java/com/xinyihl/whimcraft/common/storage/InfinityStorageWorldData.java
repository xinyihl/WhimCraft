package com.xinyihl.whimcraft.common.storage;

import appeng.api.storage.data.IAEStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InfinityStorageWorldData extends WorldSavedData {
    public static final String DATA_NAME = "whimcraft_infinity_storage";

    Map<UUID, Map<IAEStack, Long>> storageData = new HashMap<>();

    public InfinityStorageWorldData(String name) {
        super(name);
    }

    public static InfinityStorageWorldData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        InfinityStorageWorldData data = (InfinityStorageWorldData) storage.getOrLoadData(InfinityStorageWorldData.class, DATA_NAME);
        if (data == null) {
            data = new InfinityStorageWorldData(DATA_NAME);
            storage.setData(DATA_NAME, data);
        }
        return data;
    }

    public Map<IAEStack, Long> getStorage(UUID uuid) {
        return storageData.computeIfAbsent(uuid, k -> new HashMap<>());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        storageData.clear();
        NBTTagList cellList = nbt.getTagList("cells", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < cellList.tagCount(); i++) {
            NBTTagCompound cellTag = cellList.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(cellTag.getString("uuid"));
            NBTTagList itemList = cellTag.getTagList("items", Constants.NBT.TAG_COMPOUND);
            Map<IAEStack, Long> items = new HashMap<>();
            for (int j = 0; j < itemList.tagCount(); j++) {
                NBTTagCompound itemTag = itemList.getCompoundTagAt(j);
                String type = itemTag.getString("type");
                long count = itemTag.getLong("count");
                IAEStack stack = StorageDataManager.readExternalStack(type, itemTag.getCompoundTag("stack"));
                if (stack != null) {
                    items.put(stack, count);
                }
            }
            if (!items.isEmpty()) {
                storageData.put(uuid, items);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList cellList = new NBTTagList();
        for (Map.Entry<UUID, Map<IAEStack, Long>> cellEntry : storageData.entrySet()) {
            NBTTagCompound cellTag = new NBTTagCompound();
            cellTag.setString("uuid", cellEntry.getKey().toString());
            NBTTagList itemList = new NBTTagList();
            for (Map.Entry<IAEStack, Long> itemEntry : cellEntry.getValue().entrySet()) {
                IAEStack stack = itemEntry.getKey();
                Long count = itemEntry.getValue();
                if (stack != null && count != null && count > 0) {
                    NBTTagCompound itemTag = new NBTTagCompound();
                    if (!StorageDataManager.writeExternalStack(stack, itemTag)) {
                        continue;
                    }
                    itemTag.setLong("count", count);
                    itemList.appendTag(itemTag);
                }
            }
            if (itemList.tagCount() > 0) {
                cellTag.setTag("items", itemList);
                cellList.appendTag(cellTag);
            }
        }
        nbt.setTag("cells", cellList);
        return nbt;
    }
}
