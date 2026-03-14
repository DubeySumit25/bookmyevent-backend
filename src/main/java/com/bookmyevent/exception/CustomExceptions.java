package com.bookmyevent.exception;

public class CustomExceptions {

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class EventNotFoundException extends RuntimeException {
        public EventNotFoundException(String message) {
            super(message);
        }
    }

    public static class BookingNotFoundException extends RuntimeException {
        public BookingNotFoundException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class EventNotAvailableException extends RuntimeException {
        public EventNotAvailableException(String message) {
            super(message);
        }
    }

    public static class NoSeatsAvailableException extends RuntimeException {
        public NoSeatsAvailableException(String message) {
            super(message);
        }
    }

    public static class BookingDeadlinePassedException extends RuntimeException {
        public BookingDeadlinePassedException(String message) {
            super(message);
        }
    }
}