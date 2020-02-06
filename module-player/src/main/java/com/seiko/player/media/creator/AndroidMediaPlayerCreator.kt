package com.seiko.player.media.creator

import com.seiko.player.media.ijkplayer.MediaPlayerCreator
import tv.danmaku.ijk.media.player.AndroidMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer

class AndroidMediaPlayerCreator :
    MediaPlayerCreator {
    override fun createPlayer(): IMediaPlayer {
        return AndroidMediaPlayer()
    }
}