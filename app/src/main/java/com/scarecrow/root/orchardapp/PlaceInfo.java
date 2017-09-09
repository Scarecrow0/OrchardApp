package com.scarecrow.root.orchardapp;

import java.io.Serializable;

/**
 * Created by root on 17-9-9.
 */

public class PlaceInfo implements Serializable {
    public String orc_name,
            pos_url,
            orc_rules,
            ticket_remain,
            ticket_price,
            orch_info,
            orch_bannernum,
            orch_img_url,
            orch_bref,
            orch_id;

    public PlaceInfo() {
    }

    public PlaceInfo(PlaceInfo elem) {
        orc_name = elem.orc_name;
        orc_rules = elem.orc_rules;
        pos_url = elem.pos_url;
        ticket_remain = elem.ticket_remain;
        ticket_price = elem.ticket_price;
        orch_info = elem.orch_info;
        orch_img_url = elem.orch_img_url;
        orch_bref = elem.orch_bref;
        orch_bannernum = elem.orch_bannernum;
        orch_id = elem.orch_id;
    }

}
