package pl.polsl.michal.urbanek.hotelmanagementsystem.controller;

import pl.polsl.michal.urbanek.hotelmanagementsystem.model.Booking;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.BookingList;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.MenuType;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.Room;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.RoomList;
import pl.polsl.michal.urbanek.hotelmanagementsystem.model.exceptions.InvalidDataException;
import pl.polsl.michal.urbanek.hotelmanagementsystem.view.BookingView;
import pl.polsl.michal.urbanek.hotelmanagementsystem.view.RoomView;

/**
 * Controller class dealing with the processing of booking data. It provides for
 * catching exceptions and is responsible for passing the appropriate data to
 * the model and view.
 *
 * @author Michał Urbanek
 * @version 1.0
 */
public class BookingController {

    /**
     * booking list
     */
    private BookingList bookingList;
    /**
     * booking-related view class object
     */
    private BookingView viewBooking;
    /**
     * room-related view class object
     */
    private RoomView viewRoom;
    /**
     * room list
     */
    private RoomList roomlist;
    /**
     * controller class object that manages the booking
     */
    private RoomController roomController;
    /**
     * controller class object associated with data find
     */
    private RoomSearchController roomSearchController;

    /**
     * Non-parameter constructor
     */
    public BookingController() {
        this.viewBooking = new BookingView();
        this.bookingList = new BookingList();
        this.viewRoom = new RoomView();
        this.roomlist = new RoomList();
        this.roomSearchController = new RoomSearchController(roomlist, viewRoom);
        this.roomController = new RoomController(this.roomlist, this.viewRoom, roomSearchController);
    }

    /**
     * Supports the booking menu.
     */
    public void BookingMenu() {

        boolean exit = false;

        while (!exit) {

            MenuType menuType = MenuType.getType(viewBooking.showBookingMenu() - 1);

            switch (menuType) {
                case VIEW_BOOKINGS:
                    if (bookingList.getBookings().isEmpty()) {
                        viewBooking.emptyBookingListMessage();
                    } else {
                        showBookingList();
                    }
                    break;
                case ADD_BOOKING:
                    addBooking();
                    break;
                case UPDATE_BOOKING:
                    editBooking();
                    break;
                case DELETE_BOOKING:
                    deleteBooking();
                    break;
                case SHOW_ROOMS_MENU:
                    showRoomsMenu();
                    break;
                case EXIT:
                    exit = true;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Add a new booking to the list
     */
    public void addBooking() {
        viewBooking.addBookingLabel();
        int roomId = viewBooking.getRoomId(roomlist, viewRoom);
        Room room = roomlist.findByID(roomId);

        if (room != null) {
            try {
                Booking booking = new Booking(viewBooking.getFromDateOfBooking(), viewBooking.getToDateOfBooking(), room);
                bookingList.addBooking(booking);
                viewBooking.succesfullyAddedMessage();
            } catch (InvalidDataException e) {
                viewBooking.exceptionMessage(e.getMessage());
            }

        } else {
            viewBooking.nullRoomMessage(roomId);
        }

    }

    public void showRoomsMenu() {
        roomController.RoomMenuOption();
    }

    public void showBookingList() {
        if (bookingList.getBookings().isEmpty()) {
            viewBooking.emptyBookingListMessage();
        } else {
            viewBooking.showAllBookings(bookingList);
        }
    }

    /**
     * Displays a list of bookings
     */
    public void editBooking() {
        int bookingId;
        Booking bookingFromList;
        if (bookingList.getBookings().isEmpty()) {
            viewBooking.emptyBookingListMessage();
        } else {
            viewBooking.showAllBookings(bookingList);
            viewBooking.updateBookingLabel();

            bookingId = viewBooking.getRoomId(roomlist, viewRoom);
            bookingFromList = bookingList.findByID(bookingId);

            if (bookingFromList != null) {
                int roomId = viewBooking.getRoomId(roomlist, viewRoom);
                Room room = roomlist.findByID(roomId);

                if (room != null) {
                    try {
                        Booking booking = new Booking(viewBooking.getFromDateOfBooking(), viewBooking.getToDateOfBooking(), room);
                        bookingList.updateBooking(booking, bookingFromList);
                        viewBooking.succesfullyUpdateMessage();
                    } catch (InvalidDataException e) {
                        viewBooking.exceptionMessage(e.getMessage());
                    }
                } else {
                    viewBooking.nullRoomMessage(roomId);
                }

            } else {
                viewBooking.nullBookingMessage(bookingId);
            }
        }
    }

    /**
     * Deletes the booking with the ID gets in the view from the user
     */
    public void deleteBooking() {
        if (bookingList.getBookings().isEmpty()) {
            viewBooking.emptyBookingListMessage();
        } else {
            viewBooking.showAllBookings(bookingList);
            viewBooking.deleteBookingLabel();

            int id = viewBooking.getBookingId();
            Booking booking = bookingList.findByID(id);

            try {
                bookingList.deleteBooking(booking);
                viewBooking.successfullyDeletedMessage();
            } catch (InvalidDataException e) {
                viewBooking.nullBookingMessage(id);
            }
        }
    }
}
