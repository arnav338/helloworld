package parking_lot;

import parking_lot.dto.ExitDetails;
import parking_lot.dto.Ticket;
import parking_lot.enums.VehicleType;

import java.io.IOException;

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
            printCurrAvailableSlots(parkingLot);

            Ticket ticket = parkingLot.allotSlot(VehicleType.FOUR_WHEELER,"HR03AS1239");
            printTicketDetailsBanner(ticket);

            Ticket ticket1 = parkingLot.allotSlot(VehicleType.FOUR_WHEELER,"HR03AS2239");
            printTicketDetailsBanner(ticket1);

            printCurrAvailableSlots(parkingLot);

            try{
                Thread.sleep(70000);
            }catch (Exception e){
                System.out.println("Exception - "+e);
            }

            printExitBanner(parkingLot.exit("HR03AS2239"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printExitBanner(ExitDetails exit) {
        if(exit == null){
            System.out.println("********************************");
            System.out.println(" NO Vehicle entered with this number");
            System.out.println("********************************");
        }else {
            System.out.println("********************************");
            System.out.println("Vehicle exiting");
            System.out.println("Amount to pay - "+exit.getAmt());
            System.out.println("exit time - "+exit.getExitTime());
            System.out.println("entry time - "+exit.getEntryTime());
            System.out.println("vehicle number - "+exit.getVehicleNumber());
            System.out.println("********************************");
        }
    }

    private static void printTicketDetailsBanner(Ticket ticket) {
        System.out.println("********************************");
        System.out.println("Slot allotted ");
        System.out.println("level - "+ticket.getLevel());
        System.out.println("slot id - "+ticket.getSlotId());
        System.out.println("entry time - "+ticket.getEntryTime());
        System.out.println("vehicle number - "+ticket.getVehicleNumber());
        System.out.println("********************************");
    }

    private static void printCurrAvailableSlots(ParkingLot parkingLot) {
        System.out.println("********************************");
        System.out.println("Current slots available : ");
        System.out.println(parkingLot.getAllSlots());
        System.out.println("********************************");
    }
}
