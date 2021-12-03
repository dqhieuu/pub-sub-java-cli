package com.example.subui.models;

import com.example.subui.ItemFunctionality;
import com.example.subui.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemNode {
    private ItemType itemType;
    private Node node;
    private ItemFunctionality controller;
    private TopicMsg topicMsg;

    public ItemNode(ItemType itemType, Node node, ItemFunctionality controller, TopicMsg topicMsg) {
        this.itemType = itemType;
        this.node = node;
        this.controller = controller;
        this.topicMsg = topicMsg;
    }


    public void updateState(String state) {
        switch (itemType) {
            case GARAGE_SWITCH,LIGHT_SWITCH,SWITCH -> {
                try {
                    var val = Integer.parseInt(state);
                    controller.setState(val != 0);

                } catch (NumberFormatException e) {
                    System.out.println("invalid message");
                }

            }
            case VOLUME_RANGE,LIGHT_RANGE,HUMIDITY_RANGE,TEMP -> {
                try {
                    var val = Double.parseDouble(state);
                    controller.setState(val);
                } catch (NumberFormatException e) {
                    System.out.println("invalid message");
                }
            }
        }
    }

    public TopicMsg getTopicMsg() {
        return topicMsg;
    }


    public void setTopicMsg(TopicMsg topicMsg) {
        this.topicMsg = topicMsg;
        updateState(topicMsg.getMessage());
    }

    public Node getNode() {
        return node;
    }

    private static FXMLLoader getFXML(String src) {
        return new FXMLLoader(ItemNode.class.getResource(src));
    }

    public static ItemNode fromTopicMsg(TopicMsg topicMsg) throws IOException {
        ItemType itemType = Utils.getItemTypeByTopicTree(topicMsg.getSensorDetail());

        if (itemType == ItemType.NULL) return null;

        Matcher match = Pattern.compile("([\\w\\-]+)/([\\w\\-]+)/([\\w\\-]+)").matcher(topicMsg.getSensorDetail());

        if(!match.find()) return null;

        String topic = match.group(1);
        String location = match.group(2);
        String sensor = match.group(3);

        FXMLLoader loader = null;
        Node node;
        ItemFunctionality controller;
        switch (itemType) {
            case GARAGE_SWITCH -> loader = getFXML("/com/example/subui/items/item-garage-switch.fxml");
            case LIGHT_SWITCH -> loader = getFXML("/com/example/subui/items/item-light-switch.fxml");
            case SWITCH -> loader = getFXML("/com/example/subui/items/item-switch.fxml");
            case TEMP -> loader = getFXML("/com/example/subui/items/item-temperature.fxml");
            case VOLUME_RANGE -> loader = getFXML("/com/example/subui/items/item-volume.fxml");
            case LIGHT_RANGE -> loader = getFXML("/com/example/subui/items/item-light-range.fxml");
            case HUMIDITY_RANGE -> loader = getFXML("/com/example/subui/items/item-humidity.fxml");
        }
        node = loader.load();
        controller = loader.getController();
        controller.setTopic(Utils.topicToWord(topic));
        controller.setLocation(Utils.topicToWord(location));
        controller.setSensor(sensor);

        ItemNode newNode = new ItemNode(itemType, node, controller, topicMsg);
        newNode.updateState(topicMsg.getMessage());
        return newNode;
    }

    public void remove() {
        ((Pane)node.getParent()).getChildren().remove(node);
    }

    public boolean isValid() {
        return (new Timestamp(System.currentTimeMillis())).getTime() - topicMsg.getTime() <= 60000;
    }
}
