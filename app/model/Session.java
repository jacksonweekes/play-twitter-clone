package model;

/**
 * Created by jackson on 15/08/15.
 */
public class Session {
    private String id, ipAddress, location;
    private long since;

    public Session(String ipAddress) {
        this.id = java.util.UUID.randomUUID().toString();
        this.ipAddress = ipAddress;
        this.since = System.currentTimeMillis();
        this.location = findIPLocation();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getId() {
        return id;
    }

    public long getSince() {
        return since;
    }

    private String findIPLocation() {
        String location = "Here";
        return location;
    }
}
