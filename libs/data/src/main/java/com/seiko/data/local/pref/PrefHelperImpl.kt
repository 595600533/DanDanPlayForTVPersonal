package com.seiko.data.local.pref

import com.seiko.data.constants.DEFAULT_DOWNLOAD_PATH
import com.seiko.domain.pref.PrefHelper
import com.tencent.mmkv.MMKV

class PrefHelperImpl(prefs: MMKV): MMKVProperty(), PrefHelper {

    override var token by prefs.string("PREF_USER_TOKEN", "")

    override var downloadFolder by prefs.string("PREF_DOWNLOAD_FOLDER", DEFAULT_DOWNLOAD_PATH)

}