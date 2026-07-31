package org.nsu.cse215.labgroup3.pms.database.models;

import org.nsu.cse215.labgroup3.pms.database.Field;
import org.nsu.cse215.labgroup3.pms.database.Model;
import org.nsu.cse215.labgroup3.pms.database.serializers.AddressSerializer;
import org.nsu.cse215.labgroup3.pms.database.serializers.DeliveryStatusSerializer;
import org.nsu.cse215.labgroup3.pms.database.serializers.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

@Model(tagName = "parcel")
public class Parcel {
    @Field
    private String id;

    @Field
    private String description = null;

    @Field(serializer = AddressSerializer.class)
    private Address to;

    @Field(serializer = AddressSerializer.class)
    private Address from;

    /**
     * Weight in grams.
     */
    @Field
    private Double weight;

    @Field(serializer = DeliveryStatusSerializer.class)
    private DeliveryStatus status;

    @Field(serializer = ZonedDateTimeSerializer.class)
    private ZonedDateTime expectedTimeOfArrival;

    public Parcel(String id, String description, Address to, Address from, Double weight, DeliveryStatus status, ZonedDateTime expectedTimeOfArrival) {
        this.id = id;
        this.description = description;
        this.to = to;
        this.from = from;
        this.weight = weight;
        this.status = status;
        this.expectedTimeOfArrival = expectedTimeOfArrival;
    }

    public Parcel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public ZonedDateTime getExpectedTimeOfArrival() {
        return expectedTimeOfArrival;
    }

    public void setExpectedTimeOfArrival(ZonedDateTime expectedTimeOfArrival) {
        this.expectedTimeOfArrival = expectedTimeOfArrival;
    }
}
