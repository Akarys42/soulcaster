package me.akarys.soulcaster;

import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void addNewRuleToProcessorList(Identifier targetProcessorList, StructureProcessor processorToAdd, Registry<StructureProcessorList> processorListRegistry) {
        StructureProcessorList processorList = processorListRegistry.get(targetProcessorList);
        assert processorList != null;
        List<StructureProcessor> newSafeList = new ArrayList<>(processorList.getList());
        newSafeList.add(0, processorToAdd);
        processorList.list = newSafeList;
    }
}
