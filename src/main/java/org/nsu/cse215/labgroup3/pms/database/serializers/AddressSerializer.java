package org.nsu.cse215.labgroup3.pms.database.serializers;

import org.nsu.cse215.labgroup3.pms.Application;
import org.nsu.cse215.labgroup3.pms.database.XMLDataSerializer;
import org.nsu.cse215.labgroup3.pms.database.models.Address;
import org.nsu.cse215.labgroup3.pms.database.models.User;

import java.util.Optional;

public class AddressSerializer implements XMLDataSerializer<Address> {
    @Override
    public String encode(Address data) {
        return "%d|%s".formatted(data.getUser().getId(), data.getFullAddress());
    }

    @Override
    public Address decode(String data) {
        int barPosition = data.indexOf('|');
        long userId = Long.parseLong(data.substring(0, barPosition));
        String fullAddress = data.substring(barPosition + 1);
        Application application = Application.getInstance();
        Optional<User> user = application.database.getUser(userId);
        return new Address(user.orElse(null), fullAddress);
    }
}
