package org.nsu.cse215.labgroup3.pms.database;

import org.nsu.cse215.labgroup3.pms.database.models.User;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Database {
    private final ModelSerializer serializer = new ModelSerializer();
    private final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    private final Transformer transformer = TransformerFactory.newInstance().newTransformer();
    private final Map<Long, User> users = new LinkedHashMap<>();

    public Database() throws ParserConfigurationException, TransformerConfigurationException {
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

    public void save() throws TransformerException, IOException {
        Document document = builder.newDocument();

        Element database = document.createElement("database");
        document.appendChild(database);

        Element users = document.createElement("users");
        database.appendChild(users);

        for (User user : this.users.values()) {
            Element userElement = document.createElement("user");
            serializer.serialize(user, userElement);
            users.appendChild(userElement);
        }

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        try (OutputStream out = Files.newOutputStream(getDatabaseFilePath())) {
            transformer.transform(new DOMSource(document), new StreamResult(out));
        }
    }

    private void loadUsers(Node usersNode) {
        NodeList userNodes = usersNode.getChildNodes();

        for (int i = 0; i < userNodes.getLength(); i++) {
            Node userNode = userNodes.item(i);

            if (!userNode.getNodeName().equals("user"))
                continue;

            try {
                insertUser(serializer.deserialize(User.class, userNode));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void load() throws SAXException {
        try (InputStream in = Files.newInputStream(getDatabaseFilePath())) {
            Document document = builder.parse(in);
            Node database = document.getElementsByTagName("database").item(0);
            NodeList childNodes = database.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);

                switch (childNode.getNodeName()) {
                    case "users":
                        loadUsers(childNode);
                        break;

                    default:
                        break;
                }
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    protected Path getDatabaseFilePath() {
        String homeDirectory = System.getProperty("user.home");
        return Path.of(homeDirectory, ".pms-db.xml");
    }
}
