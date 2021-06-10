package pl.polsl.michal.urbanek.hotelmanagementsystem.controller;

import pl.polsl.michal.urbanek.hotelmanagementsystem.model.Room;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.RoomList;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.exceptions.InvalidDataException;
import pl.polsl.michal.urbanek.hotelmanagementsystem.view.RoomView;

/**
 * Controller class dealing with the processing of room data. It provides for
 * catching exceptions and is responsible for passing the appropriate data to
 * the model and view.
 *
 * @author Michał Urbanek
 * @version 1.0
 */
public class RoomController {

    /**
     * Room list
     */
    private RoomList rooms;
    /**
     * room-related view class object
     */
    private RoomView viewRoom;
    /**
     * controller class object associated with data find
     */
    private RoomSearchController roomSearchController;

    /**
     * Constructor
     *
     * @param rooms room list
     * @param viewRoom room-related view class object
     * @param roomSearchController controller class object with data find
     */
    public RoomController(RoomList rooms, RoomView viewRoom, RoomSearchController roomSearchController) {
        this.rooms = rooms;
        this.viewRoom = viewRoom;
        this.roomSearchController = roomSearchController;
    }

    /**
     * Returns a new object based on the data retrieved from the view from the
     * user.
     *
     * @return a new room object
     */
    public Room CreateRoom() {
        int roomNumber = viewRoom.getRoomNumberFromConsole();
        int floor = viewRoom.getFloorFromConsole();
        double price = viewRoom.getPriceFromConsole();
        int roomType = viewRoom.getNumberOfPeopleFromConsole();

        return new Room(roomNumber, roomType, floor, price);
    }

    /**
     * Displays a list of all rooms
     */
    public void showRoomList() {
        if (rooms.getRooms().isEmpty()) {
            viewRoom.emptyRoomListMessage();

        } else {
            viewRoom.showAllRooms(rooms);
        }
    }

    /**
     * Add a new room to the list
     */
    public void AddRoom() {
        viewRoom.addRoomLabel();
        Room room = CreateRoom();

        try {
            rooms.addRoom(room);
            viewRoom.succesfullyAddedMessage();
        } catch (InvalidDataException e) {
            viewRoom.nullRoomMessage();
        }
    }

    /**
     * Supports the room menu.
     */
    public void RoomMenuOption() {
        int choice = 0;

        while (choice != 6) {
            choice = viewRoom.showRoomMenu();

            switch (choice) {
                case 1:
                    showRoomList();
                    break;
                case 2:
                    AddRoom();
                    break;
                case 3:
                    editRoom();
                    break;
                case 4:
                    deleteRoom();
                    break;
                case 5:
                    roomSearchController.RoomSearchMenu();
                    break;
                default:
                    break;

            }

        }
    }

    /**
     * Edits the room with the ID gets in the view from the user
     */
    public void editRoom() {
        if (rooms.getRooms().isEmpty()) {
            viewRoom.emptyRoomListMessage();
        } else {
            viewRoom.showAllRooms(rooms);
            viewRoom.updateRoomMessage();

            int roomId = viewRoom.getRoomIdFromConsole();
            Room editedRoom = rooms.findByID(roomId);
            if (editedRoom != null) {
                try {
                    Room room = CreateRoom();
                    rooms.updateRoom(room, editedRoom);
                    viewRoom.succesfullyUpdateMessage();
                } catch (InvalidDataException e) {
                    viewRoom.nullRoomMessage();
                }

            } else {
                viewRoom.nullRoomMessage(roomId);
            }
        }
    }

    /**
     * Deletes the room with the ID gets in the view from the user
     */
    public void deleteRoom() {
        if (rooms.getRooms().isEmpty()) {
            viewRoom.emptyRoomListMessage();
        } else {
            viewRoom.showAllRooms(rooms);
            viewRoom.deleteRoomLabel();

            int id = viewRoom.getRoomIdFromConsole();
            Room room = rooms.findByID(id);
            try {
                rooms.deleteRoom(room);
                viewRoom.succesfullyDeletedMessage();
            } catch (InvalidDataException e) {
                viewRoom.nullRoomMessage(id);
            }
        }
    }

}
