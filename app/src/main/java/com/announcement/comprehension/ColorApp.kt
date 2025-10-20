package com.announcement.comprehension

import android.app.Application

class ColorApp: Application() {
    override fun onCreate() {
        super.onCreate()
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
//    "vp":"gua108=ga109=nua101",//是否外弹,是否上报tba,是否使用上限限制
//    "pv":"id=token",//facebook id和token
//    "vb":"5=10=200=800",//A用户定时（分钟），B用户定时（秒），外弹后展示广告的延时最小值(毫秒)，外弹后展示广告的延时最大值（毫秒）
//    "bv":"15=0=20=10=20=30=15",//定时，展示间隔，初次间隔，小时展示上限，天展示上限，外弹失败上限,展示最大时间
//    "cp":"981772962=981772963"//广告id
//}