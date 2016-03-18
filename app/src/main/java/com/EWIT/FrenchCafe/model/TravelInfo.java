package com.EWIT.FrenchCafe.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Euro on 3/18/16 AD.
 */
public class TravelInfo {

    /**
     * destination_addresses : ["วิกตอเรีย บริติชโคลัมเบีย แคนาดา"]
     * origin_addresses : ["แวนคูเวอร์ วอชิงตัน สหรัฐอเมริกา"]
     * rows : [{"elements":[{"distance":{"text":"396 กม.","value":396176},"duration":{"text":"5 ชั่วโมง 59 นาที","value":21540},"status":"OK"}]}]
     * status : OK
     */

    @SerializedName("status") public String status;
    @SerializedName("destination_addresses") public List<String> destinationAddresses;
    @SerializedName("origin_addresses") public List<String> originAddresses;
    @SerializedName("rows") public List<RowsEntity> rows;

    public static class RowsEntity {
        /**
         * distance : {"text":"396 กม.","value":396176}
         * duration : {"text":"5 ชั่วโมง 59 นาที","value":21540}
         * status : OK
         */

        @SerializedName("elements") public List<ElementsEntity> elements;

        public static class ElementsEntity {
            /**
             * text : 396 กม.
             * value : 396176
             */

            @SerializedName("distance") public DistanceEntity distance;
            /**
             * text : 5 ชั่วโมง 59 นาที
             * value : 21540
             */

            @SerializedName("duration") public DurationEntity duration;
            @SerializedName("status") public String status;

            public static class DistanceEntity {
                @SerializedName("text") public String text;
                @SerializedName("value") public int value;
            }

            public static class DurationEntity {
                @SerializedName("text") public String text;
                @SerializedName("value") public int value;
            }
        }
    }
}
