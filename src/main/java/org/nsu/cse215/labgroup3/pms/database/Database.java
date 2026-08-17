package org.nsu.cse215.labgroup3.pms.database;

import org.nsu.cse215.labgroup3.pms.database.models.Parcel;
import org.nsu.cse215.labgroup3.pms.database.models.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Database {
    private final ModelSerializer serializer = new ModelSerializer();
    private final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    private final Transformer transformer = TransformerFactory.newInstance().newTransformer();
    private final Map<Long, User> users = new LinkedHashMap<>();
    private final Map<String, Parcel> parcels = new LinkedHashMap<>();
    private Long nextUserId = 0L;

    public Database() throws ParserConfigurationException, TransformerConfigurationException {
    }

    public boolean insertParcel(Parcel parcel) {
        String id = parcel.getId();

        if (parcels.containsKey(id)) {
            return false;
        }

        parcels.put(id, parcel);
        return true;
    }

    public void save() throws TransformerException, IOException {
        Document document = builder.newDocument();

        Element database = document.createElement("database");
        document.appendChild(database);

        Element users = document.createElement("users");
        database.appendChild(users);

        Element parcels = document.createElement("parcels");
        database.appendChild(parcels);

        for (User user : this.users.values()) {
            Element userElement = serializer.serialize(user, document);
            users.appendChild(userElement);
        }

        for (Parcel parcel : this.parcels.values()) {
            Element parcelElement = serializer.serialize(parcel, document);
            parcels.appendChild(parcelElement);
        }

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        try (FileWriter writer = new FileWriter(getDatabaseFilePath().toString(), StandardCharsets.UTF_8)) {
            transformer.transform(new DOMSource(document), new StreamResult(writer));
        }
    }

    private void loadUsers(Node usersNode) {
        NodeList userNodes = usersNode.getChildNodes();
        Long maxId = 0L;

        for (int i = 0; i < userNodes.getLength(); i++) {
            Node userNode = userNodes.item(i);

            if (!userNode.getNodeName().equals("user")) {
                continue;
            }

            try {
                User user = serializer.deserialize(User.class, userNode);
                insertUser(user);

                if (maxId < user.getId()) {
                    maxId = user.getId();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        nextUserId = maxId + 1;
    }

    private void loadParcels(Node parcelsNode) {
        NodeList parcelNodes = parcelsNode.getChildNodes();

        for (int i = 0; i < parcelNodes.getLength(); i++) {
            Node parcelNode = parcelNodes.item(i);

            if (!parcelNode.getNodeName().equals("parcel")) {
                continue;
            }

            try {
                insertParcel(serializer.deserialize(Parcel.class, parcelNode));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void load() throws SAXException {
        try (FileReader reader = new FileReader(getDatabaseFilePath().toString())) {
            Document document = builder.parse(new InputSource(reader));
            Node database = document.getElementsByTagName("database").item(0);
            NodeList childNodes = database.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);

                switch (childNode.getNodeName()) {
                    case "users":
                        loadUsers(childNode);
                        break;

                    case "parcels":
                        loadParcels(childNode);
                        break;

                    default:
                        break;
                }
            }
        }
        catch (IOException exception) {
            exception.printStackTrace(System.err);
        }
    }

    public Optional<User> findUserByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.getOrDefault(id, null));
    }

    public boolean insertUser(User user) {
        long id = user.getId();

        if (users.containsKey(id)) {
            return false;
        }

        users.put(id, user);
        return true;
    }

    public Optional<Parcel> getParcel(String id) {
        return Optional.ofNullable(parcels.getOrDefault(id, null));
    }

    protected Path getDatabaseFilePath() {
        String homeDirectory = System.getProperty("user.home");
        return Path.of(homeDirectory, ".pms-db.xml");
    }

    public Long nextUserId() {
        return ++nextUserId;
    }

    public Parcel deleteParcel(String previousTrackingID) {
        return parcels.remove(previousTrackingID);
    }

    public Map<String, Parcel> getAllParcels() {
        return parcels;
    }

    public User deleteUser(Long id) {
        return users.remove(id);
    }
}
