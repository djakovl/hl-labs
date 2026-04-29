package digital.zil.hl.module1.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaMessage {

    private String entity;    // "STUDENT", "COURSE", "ENROLLMENT"
    private String operation; // "POST", "PUT", "DEL"
    private String payload;   // JSON-строка или plain id

    public KafkaMessage() {}

    public String getEntity() { return entity; }
    public void setEntity(String entity) { this.entity = entity; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }

    @Override
    public String toString() {
        return "KafkaMessage{entity='%s', operation='%s'}".formatted(entity, operation);
    }
}