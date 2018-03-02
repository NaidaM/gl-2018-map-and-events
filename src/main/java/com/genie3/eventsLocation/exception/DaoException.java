package com.genie3.eventsLocation.exception;

public final class DaoException {

    public static class NotFoundException extends Exception {
        private String message;
        public NotFoundException(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class DaoInternalError extends Exception{
        private String message;
        public DaoInternalError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
