package ro.mariuscirstea.template.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity(name = "evt_event")
@Table(name = "evt_event")
@NamedQuery(name = EVT_Event.QUERY_BETWEEN_DATES, query = "SELECT e FROM evt_event e " +
        "WHERE e.created >= :" + EVT_Event.START_TIMESTAMP + " and e.created <= :" + EVT_Event.END_TIMESTAMP)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EVT_Event {

    public static final String QUERY_BETWEEN_DATES = "query.Event.betweenDates";

    public static final String START_TIMESTAMP = "startTimestamp";

    public static final String END_TIMESTAMP = "endTimestamp";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private String type;

    private Timestamp created;

    private Timestamp updated;

    private Long userId;

    private String ip;

    private String additionalInfo; //json format?

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", userId=" + userId +
                ", ip='" + ip + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }

    public static class EventBuilder {
        private Long id;
        private String description;
        private String type;
        private Timestamp created;
        private Timestamp updated;
        private Long userId;
        private String ip;
        private String additionalInfo; //json format?

        public EventBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder type(String type) {
            this.type = type;
            return this;
        }

        public EventBuilder createdTimestamp(Timestamp created) {
            this.created = created;
            return this;
        }

        public EventBuilder lastUpdatedTimestamp(Timestamp updated) {
            this.updated = updated;
            return this;
        }

        public EventBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public EventBuilder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public EventBuilder additionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
            return this;
        }

        public EventBuilder concatenateAdditionalInfo(String yetAdditionalInfo) {
            if (this.additionalInfo != null) {
                this.additionalInfo = this.additionalInfo.concat(yetAdditionalInfo);
            } else {
                this.additionalInfo = yetAdditionalInfo;
            }
            return this;
        }

        public EVT_Event build() {
            EVT_Event event = new EVT_Event();
            if (this.id != null) {
                event.setId(this.id);
            }
            if (this.description != null) {
                event.setDescription(this.description);
            }
            if (this.type != null) {
                event.setType(this.type);
            }
            if (this.created != null) {
                event.setCreated(this.created);
            }
            if (this.updated != null) {
                event.setUpdated(this.updated);
            }
            if (this.userId != null) {
                event.setUserId(this.userId);
            }
            if (this.ip != null) {
                event.setIp(this.ip);
            }
            if (this.additionalInfo != null) {
                event.setAdditionalInfo(this.additionalInfo);
            }
            return event;
        }

    }
}
