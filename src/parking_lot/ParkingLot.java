package parking_lot;

import parking_lot.dto.Level;
import parking_lot.dto.Ticket;
import parking_lot.enums.VehicleType;
import parking_lot.util.LoaderUtil;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ParkingLot {

    private final LoaderUtil loader = new LoaderUtil();

    private Map<Integer,Level> levelMap = new HashMap<>();

    static ParkingLot parkingLot;

    public static ParkingLot init(String configFile) throws IOException {
        if(parkingLot==null){
            parkingLot = new ParkingLot(configFile);
        }
        else {
            System.out.println("Already running instance");
        }
        return parkingLot;
    }

    private ParkingLot(String configFile) throws IOException {
        System.out.println("Loading properties");
        Properties properties = null;
        try {
         properties = loader.loadAndFetchProperties(configFile);
        }catch (Exception e){
            System.out.println("Exception while loading properties - "+properties);
        }
        System.out.println("Properties loaded. Please wait, Getting system ready !!");
        boolean valid = validateProperties(properties);
        if(!valid){
            System.out.println("Invalid properties");
        }else {
            System.out.println("Initialised parking lot with "+properties.getProperty("totalLevels")+" levels");
        }
    }

    private boolean validateProperties(Properties properties) {
        if(properties == null || !properties.containsKey("totalLevels")){
            return false;
        }
        int levels = Integer.parseInt(properties.getProperty("totalLevels"));
        for(int i=1; i<=levels; i++){
            boolean val = validateLevelWiseDetails("level"+ i,properties);
            if(!val){
                return false;
            }
        }
        return true;
    }

    private boolean validateLevelWiseDetails(String level, Properties properties) {
        AtomicInteger totalTypesInLevel = new AtomicInteger();
        AtomicInteger totalSlots = new AtomicInteger();
        totalTypesInLevel.set(-1);
        totalSlots.set(-1);
        Map<String,Object> map = new HashMap<>();
        List<Map.Entry<Object, Object>> props = properties.entrySet()
                .stream()
                .filter(obj -> ((String) obj.getKey()).startsWith(level))
                .peek(entry -> {
                    if(((String)entry.getKey()).contains("totalTypes")){
                        totalTypesInLevel.set(Integer.parseInt((String) entry.getValue()));
                    } else if (((String) entry.getKey()).contains("totalSlots")) {
                        totalSlots.set(Integer.parseInt((String) entry.getValue()));
                    }else{
                        map.put(((String)entry.getKey()).substring(level.length()+1),entry.getValue());
                    }
                })
                .toList();
        System.out.println("map : "+map);
        Level lev = new Level();
        Map<VehicleType,Integer> vMap = new HashMap<>();
        lev.setTotalSlots(totalSlots.get());
        for(int i=1; i<=totalTypesInLevel.get(); i++){
            String vehicleType = (String) map.get("type" + i + ".name");
            String slots = (String) map.get("type" + i + ".slots");
            System.out.println(slots+" for "+vehicleType);
            lev.getTypeSlots().put(VehicleType.valueOf(vehicleType),Integer.valueOf(slots));
        }
        lev.setId(Integer.parseInt(""+level.charAt(level.length()-1)));
        levelMap.put(lev.getId(),lev);
        return true;
    }

    /**
     Check if any slot of the passed 'type' is available
     in the parking lot
    * */
    public boolean isSlotAvailable(VehicleType type){
        Integer reduce = levelMap.values()
                .stream()
                .flatMap(l -> l.getTypeSlots().entrySet().stream())
                .filter(e -> type.equals(e.getKey()))
                .map(Map.Entry::getValue)
                .reduce(0, Integer::sum);
        return reduce > 0;
    }

    public Map<VehicleType,Integer> getAllSlots(){
        return levelMap.values()
                .stream()
                .flatMap(l -> l.getTypeSlots().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum))
                ;
    }

    public void allotSlot(VehicleType vehicleType, Ticket ticket) {

    }
}
