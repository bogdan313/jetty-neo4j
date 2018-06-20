package helpers;

/**
 * Helper class for defining some constants, which often uses in project
 */
public class Constants {
    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf8";
    public static final int ELEMENTS_PER_PAGE = 10;

    public enum STATUSES {
        WAITING("Waiting"),
        ACCEPTED("Accepted"),
        DECLINED("Declined");

        private final String value;

        STATUSES(String status) {
            this.value = status;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static boolean isAvailable(String status) {
            return status.equals(STATUSES.WAITING.getValue()) ||
                    status.equals(STATUSES.ACCEPTED.getValue()) ||
                    status.equals(STATUSES.DECLINED.getValue());
        }
    }
}
