package ru.javarush.kornienko.island.services.statistics;

public enum ShortInfoType {
    REPRODUCE_INFO("Никто не размножился", "размножилось", "Родившиеся", "животных"),
    MOVE_INFO("Никто не двигался", "переместилось", "Перемещенные", "животных"),
    DIE_INFO("Никто не умер от голода", "умерло от голода", "Умершие", "животных"),
    GROWN_INFO("Растения не выросли", "выросло", "Выросшие", "растений");

    private final String negativeInfo;
    private final String action;
    private final String descriptive;
    private final String organismType;

    ShortInfoType(String negativeInfo, String action, String descriptive, String organismType) {
        this.negativeInfo = negativeInfo;
        this.action = action;
        this.descriptive = descriptive;
        this.organismType = organismType;
    }

    public String getNegativeInfo() {
        return negativeInfo;
    }

    public String getAction() {
        return action;
    }

    public String getDescriptive() {
        return descriptive;
    }

    public String getOrganismType() {
        return organismType;
    }
}
