package com.seiko.player.domain.danma

import com.seiko.common.data.Result
import com.seiko.player.data.db.model.VideoDanmaku
import com.seiko.player.data.comments.VideoDanmaRepository
import com.seiko.player.data.model.DanmaCommentBean
import com.seiko.player.data.model.PlayParam
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException

/**
 * 获取弹幕集合
 */
class GetDanmaCommentsUseCase : KoinComponent {

    private val danmaDbRepo: VideoDanmaRepository by inject()
    private val downloadDanma: DownloadDanmaUseCase by inject()

    suspend operator fun invoke(param: PlayParam): Result<List<DanmaCommentBean>> {
        // 判断视频是否存在
        val videoFile = File(param.videoPath)
        if (!videoFile.exists()) {
            return Result.Error(FileNotFoundException("Not found file: ${param.videoPath}"))
        }

        // 获得视频md5
        val videoMD5 = param.videoMd5

        // 尝试从本地数据库获取弹幕
        var start = System.currentTimeMillis()
        when(val result = danmaDbRepo.getDanmaDownloadBean(videoMD5)) {
            is Result.Success -> {
                Timber.d("danma from db, 耗时：${System.currentTimeMillis() - start}")
                return Result.Success(result.data)
            }
        }

        // 下载弹幕
        start = System.currentTimeMillis()
        return when(val result = downloadDanma.invoke(param)) {
            is Result.Success -> {
                Timber.d("danma from net, 耗时：${System.currentTimeMillis() - start}")
                // 保存到数据库
                danmaDbRepo.saveDanmaDownloadBean(VideoDanmaku(
                    videoMd5 = videoMD5,
                    danma = result.data
                ))
                result
            }
            is Result.Error -> {
                result
            }
        }
    }

}