package com.example.subui.utils;

import com.example.subui.models.ItemType;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class Utils {
    private static final Map<String, String> topicToWord = Map.ofEntries(
            entry("BEDROOM", "Phòng ngủ"),
            entry("LIVING_ROOM", "Phòng khách"),
            entry("DINING_ROOM", "Phòng ăn"),
            entry("BATHROOM", "Nhà tắm"),
            entry("CORRIDOR", "Hành lang"),
            entry("YARD", "Sân"),
            entry("KITCHEN", "Phòng bếp"),
            entry("BASEMENT", "Tầng hầm"),
            entry("GARAGE", "Garage"),
            entry("STUDY", "Phòng làm việc"),
            entry("HALL", "Sảnh"),
            entry("BALCONY", "Ban công"),
            entry("BACKYARD", "Sân sau"),
            entry("ROOF", "Mái nhà"),
            entry("AC_TEMP", "Điều hoà"),
            entry("FRIDGE_TEMP", "Tủ lạnh"),
            entry("RADIATOR_TEMP", "Máy sưởi"),
            entry("WATER_HEATER_TEMP", "Bình nóng lạnh"),
            entry("ROOM_TEMP", "Nhiệt độ"),
            entry("TEMP", "Nhiệt độ"),
            entry("LIGHT_SWITCH", "Bóng đèn"),
            entry("TV_SWITCH", "TV"),
            entry("SPEAKER_SWITCH", "Loa"),
            entry("AC_SWITCH", "Điều hoà"),
            entry("PC_SWITCH", "Máy tính"),
            entry("FRIDGE_SWITCH", "Tủ lạnh"),
            entry("GARAGE_SWITCH", "Cửa garage"),
            entry("HVAC_SWITCH", "Hệ thống HVAC"),
            entry("CAMERA_SWITCH", "Camera"),
            entry("CIRCUIT_SWITCH", "Cầu dao"),
            entry("WATER_HEATER_SWITCH", "Bình nóng lạnh"),
            entry("SWITCH", "Công tắc"),
            entry("TV_VOLUME_RANGE", "Âm lượng TV"),
            entry("SPEAKER_VOLUME_RANGE", "Âm lượng loa"),
            entry("VOLUME_RANGE", "Âm lượng"),
            entry("HUMIDITY_RANGE", "Độ ẩm"),
            entry("LIGHT_RANGE", "Bóng đèn"),
            entry("RANGE", "Mức độ")
            );

    public static String topicToWord(String topic) {
        return Objects.requireNonNullElse(Utils.topicToWord.get(topic), topic);
    }

    public static ItemType getItemTypeByTopicTree(String str) {
        Matcher match = Pattern.compile("([\\w\\-]+)/[\\w\\-]+/[\\w\\-]+").matcher(str);
        ItemType detectedType = ItemType.NULL;
        if(match.find()) {
            String topic = match.group(1);
            if(topic.endsWith("SWITCH")) {
                detectedType = ItemType.SWITCH;
                if(topic.equals("GARAGE_SWITCH")) {
                    detectedType = ItemType.GARAGE_SWITCH;
                } else if(topic.equals("LIGHT_SWITCH")) {
                    detectedType = ItemType.LIGHT_SWITCH;
                }
            } else if (topic.endsWith("TEMP")) {
                detectedType = ItemType.TEMP;
            } else if (topic.endsWith("RANGE")) {
                detectedType = switch (topic) {
                    case "VOLUME_RANGE" -> ItemType.VOLUME_RANGE;
                    case "HUMIDITY_RANGE" -> ItemType.HUMIDITY_RANGE;
                    case "LIGHT_RANGE" -> ItemType.LIGHT_RANGE;
                    default -> detectedType;
                };
            }
        }
        return detectedType;
    }
}
