package parking_lot;

import parking_lot.dto.*;
import parking_lot.enums.TicketStatus;
import parking_lot.enums.VehicleType;
import parking_lot.util.LoaderUtil;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ParkingLot {

    private final LoaderUtil loader = new LoaderUtil();

    private final Map<Integer,Level> levelMap = new HashMap<>();

    private final Map<VehicleType, Fare> fareMap = new HashMap<>();

    static ParkingLot parkingLot;

    private final Map<String,Ticket> allottedTickets = new HashMap<>();

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
        properties.entrySet()
                .stream()
                .filter(e -> ((String)e.getKey()).contains("type.fare"))
                .forEach(e -> {
                    if(((String)e.getKey()).contains("base")){
                        String s = ((String)e.getKey()).substring(15);
                        System.out.println("s - "+s);
                        VehicleType vehicleType = VehicleType.valueOf(s);
                        if(!fareMap.containsKey(vehicleType)){
                            fareMap.put(vehicleType,new Fare());
                        }
                        fareMap.get(vehicleType).setBase(Integer.parseInt((String) e.getValue()));
                    }else{
                        String s = ((String)e.getKey()).substring(15);
                        System.out.println("s - "+s);
                        VehicleType vehicleType = VehicleType.valueOf(s);
                        if(!fareMap.containsKey(vehicleType)){
                            fareMap.put(vehicleType,new Fare());
                        }
                        fareMap.get(vehicleType).setHourly(Integer.parseInt((String) e.getValue()));
                    }
                });
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
        lev.setTotalSlots(totalSlots.get());
        for(int i=1; i<=totalTypesInLevel.get(); i++){
            String vehicleType = (String) map.get("type" + i + ".name");
            String slots = (String) map.get("type" + i + ".slots");
            System.out.println(slots+" for "+vehicleType);
            lev.getTypeSlots().put(VehicleType.valueOf(vehicleType),Integer.valueOf(slots));
            lev.getCurrSlotNumber().put(VehicleType.valueOf(vehicleType),1);
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

    /**
     Give all available slots of all types in the form of map
     * */
    public Map<VehicleType,Integer> getAllSlots(){
        return levelMap.values()
                .stream()
                .flatMap(l -> l.getTypeSlots().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum))
                ;
    }

    public Ticket allotSlot(VehicleType vehicleType, String vehicleNumber) {
        Ticket ticket = new Ticket();
        if(!isSlotAvailable(vehicleType)){
            ticket.setStatus(TicketStatus.MAX_OCCUPANCY_REACHED);
            return ticket;
        }
        ticket.setId(UUID.randomUUID().toString());
        ticket.setVehicleNumber(vehicleNumber);
        provisionSlot(vehicleType,ticket);
        ticket.setStatus(TicketStatus.ALLOTTED);
        ticket.setType(vehicleType);
        allottedTickets.put(ticket.getVehicleNumber(),ticket);
        return ticket;
    }

    private void provisionSlot(VehicleType vehicleType, Ticket ticket) {
        Slot slot = getNextSlotAvailable(vehicleType,ticket);
        if(slot == null && TicketStatus.CAN_NOT_FIND_SLOT.equals(ticket.getStatus())){
            return;
        }
        ticket.setSlotId(slot.getSlotId());
        ticket.setLevel(slot.getLevel());
        ticket.setEntryTime(System.currentTimeMillis());
    }

    private Slot getNextSlotAvailable(VehicleType vehicleType, Ticket ticket) {
        Optional<Map.Entry<Integer, Level>> first = levelMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getCurrSlotNumber().get(vehicleType) < entry.getValue().getTypeSlots().get(vehicleType))
                .findFirst();
        if(first.isEmpty()){
            ticket.setStatus(TicketStatus.CAN_NOT_FIND_SLOT);
            return null;
        }
        Slot slot = new Slot();
        slot.setLevel(first.get().getKey());
        slot.setSlotId(first.get().getValue().getCurrSlotNumber().get(vehicleType));
        levelMap.get(first.get().getKey()).getCurrSlotNumber().put(vehicleType,levelMap.get(first.get().getKey()).getCurrSlotNumber().get(vehicleType)+1);
        levelMap.get(first.get().getKey()).getTypeSlots().put(vehicleType,levelMap.get(first.get().getKey()).getTypeSlots().get(vehicleType)-1);
        return slot;
    }

    public ExitDetails exit(String vehicleNumber) {
        ExitDetails exitDetails = new ExitDetails();
        if(!allottedTickets.containsKey(vehicleNumber)){
            return null;
        }
        Ticket ticket = allottedTickets.get(vehicleNumber);
        calculateFare(ticket,exitDetails,System.currentTimeMillis());
        return exitDetails;
    }

    private void calculateFare(Ticket ticket, ExitDetails exitDetails, long exitTime) {
        long duration = (exitTime - ticket.getEntryTime())/1000;
        int min = ((int) (duration/60))+1;
        int sec = (int) (duration%60);
        System.out.println("min : "+min+", sec : "+sec);
        double amt = 0;
        amt += fareMap.get(ticket.getType()).getBase() + min*fareMap.get(ticket.getType()).getHourly();
        exitDetails.setAmt(amt);
        exitDetails.setExitTime(exitTime);
        exitDetails.setVehicleNumber(ticket.getVehicleNumber());
        exitDetails.setEntryTime(ticket.getEntryTime());
        exitDetails.setId(ticket.getId());
    }
}
