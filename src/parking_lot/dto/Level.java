package parking_lot.dto;

import parking_lot.enums.VehicleType;

import java.util.HashMap;
import java.util.Map;

public class Level {
    int id;
    public int totalSlots;
    public Map<VehicleType,Integer> typeSlots;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Level() {
        this.typeSlots = new HashMap<>();
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Map<VehicleType, Integer> getTypeSlots() {
        return typeSlots;
    }

    public void setTypeSlots(Map<VehicleType, Integer> typeSlots) {
        this.typeSlots = typeSlots;
    }
}
