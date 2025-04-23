package parking_lot;

import parking_lot.dto.Ticket;
import parking_lot.enums.VehicleType;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Runner {
    public static void main(String[] args) {
        try {
            System.out.println("********************************");
            System.out.println("********************************");
            System.out.println("#####Welcome to parking lot#####");
            System.out.println("********************************");
            System.out.println("********************************");

            ParkingLot parkingLot = ParkingLot.init("config.txt");

            System.out.println("is 4 wheeler slot available ? "+parkingLot.isSlotAvailable(VehicleType.FOUR_WHEELER));
            Map<VehicleType, Integer> allSlots = parkingLot.getAllSlots();
            System.out.println("********************************");
            System.out.println("Current slots available : ");
            System.out.println(allSlots);
            System.out.println("********************************");

            Ticket ticket = new Ticket();
            ticket.setId(UUID.randomUUID().hashCode());
            parkingLot.allotSlot(VehicleType.FOUR_WHEELER,ticket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
