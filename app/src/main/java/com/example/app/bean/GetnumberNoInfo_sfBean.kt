package com.example.app.bean

data class GetnumberNoInfo_sfBean(
        val errorCode: String,
        val data: List<Data>
)

data class Data(
        val Advert_Position: List<AdvertPosition>,
        val ChanneType: List<ChanneType>,
        val Country: List<Country>,
        val Member: List<Member>,
        val GetChanneOnelList: List<GetChanneOnel>,
        val Account_Catalog: List<AccountCatalog>
)

data class AdvertPosition(
        val ID: Int,
        val PositionName: String,
        val imgurl: String,
        val linkurl: String,
        val adtype: Int
)

data class Member(
        val ID: Int,
        val headimgurl: String,
        val RealName: Any,
        val Mobile: String,
        val Nickname: String,
        val Channel_Name: String,
        val int_products_count: Int,
        val user_Integral: Int,
        val is_Friends: Int,
        val authID: Int
)

data class ChanneType(
        val ID: Int,
        val name: String,
        val Channel_icon: String
)

data class Country(
        val ID: Int,
        val Ccname: String,
        val Cename: String,
        val National_Flag: String,
        val picUrl: String
)

data class AccountCatalog(
        val ID: Int,
        val parent_ID: Int,
        val Channel_Name: String,
        val Channel_Name_mobile: String,
        val Channel_Name_English: String,
        val Channel_icon: String,
        val URL_GoTo: String
)

data class GetChanneOnel(
        val ID: Int,
        val Channel_Name: String,
        val URL_GoTo: String,
        val ChannelTwo: List<ChannelTwo>
)

data class ChannelTwo(
        val ID: Int,
        val Channel_Name: String,
        val URL_GoTo: String
)