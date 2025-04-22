package parking_lot;

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
            parkingLot.allotSlot(VehicleType.FOUR_WHEELER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
