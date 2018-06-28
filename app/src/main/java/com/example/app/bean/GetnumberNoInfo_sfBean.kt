package com.example.app.bean

class GetnumberNoInfo_sfBean {
    var errorCode: String? = null
    var data: List<Data>? = null

    inner class Advert_Position {
        var id: Int = 0
        var positionName: String? = null
        var imgurl: String? = null
        var linkurl: String? = null
        var adtype: Int = 0
    }

    inner class ChanneType {
        var id: Int = 0
        var channel_Name: String? = null
        var channel_icon: String? = null
        var type: Int = 0
    }

    inner class Country {
        var id: Int = 0
        var ccname: String? = null
        var cename: String? = null
        var national_Flag: String? = null
        var picUrl: String? = null
    }

    inner class Member {
        var id: Int = 0
        var headimgurl: String? = null
        var realName: String? = null
        var mobile: String? = null
        var nickname: String? = null
        var channel_Name: String? = null
        var int_products_count: Int = 0
        var user_Integral: Int = 0
        var is_Friends: Int = 0
        var authID: Int = 0
    }

    inner class ChannelTwo {
        var id: Int = 0
        var channel_Name: String? = null
        var urL_GoTo: String? = null
    }

    inner class GetChanneOnelList {
        var id: Int = 0
        var channel_Name: String? = null
        var urL_GoTo: String? = null
        var channelTwo: List<ChannelTwo>? = null
    }

    inner class Account_Catalog {
        var id: Int = 0
        var parent_ID: Int = 0
        var channel_Name: String? = null
        var channel_Name_mobile: String? = null
        var channel_Name_English: String? = null
        var channel_icon: String? = null
        var urL_GoTo: String? = null
    }

    inner class Data {
        var advert_Position: List<Advert_Position>? = null
        var channeType: List<ChanneType>? = null
        var country: List<Country>? = null
        var member: List<Member>? = null
        var getChanneOnelList: List<GetChanneOnelList>? = null
        var account_Catalog: List<Account_Catalog>? = null
    }
}