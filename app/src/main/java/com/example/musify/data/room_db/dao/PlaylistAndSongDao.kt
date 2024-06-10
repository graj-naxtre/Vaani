package com.example.musify.data.room_db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.musify.data.room_db.entity.Playlist
import com.example.musify.data.room_db.entity.PlaylistSongCrossRef
import com.example.musify.data.room_db.entity.PlaylistWithSongs
import com.example.musify.data.room_db.entity.Song

@Dao
interface PlaylistAndSongDao {
    // create a playlist, return playlistId
    @Insert
    suspend fun createPlaylist(playlist: Playlist) : Long

    @Query("SELECT * FROM Playlist")
    suspend fun getAllPlaylist() : List<Playlist>

    // insert song, return songId
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song) : Long

    // get Song by Id
    @Query("SELECT * FROM Song WHERE mediaId = :mediaId")
    suspend fun getSongId(mediaId: Long) : Song

    @Query("SELECT mediaId FROM Song WHERE media_file_path = :filePath")
    suspend fun getMediaId(filePath: String) : Long

    // add song to Playlist using songId and playlistId
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)

    // remove song from playlist
    @Transaction
    @Delete
    suspend fun removeSongFromPlaylist(crossRef: PlaylistSongCrossRef){
        _removeSongIfNotInAnyPlaylist(mediaId = crossRef.mediaId)
    }

    // delete song
    @Query("DELETE FROM Song WHERE mediaId = :mediaId")
    suspend fun _deleteSongById(mediaId: Long)

    // remove song if not present in any playlist
    @Transaction
    suspend fun _removeSongIfNotInAnyPlaylist(mediaId: Long){
        val count = _getPlaylistContainingSong(mediaId)
        if(count == 0){
            // if not present then delete
            _deleteSongById(mediaId)
        }
    }

    // count no. of playlist containing a particular song with media Id
    @Query("SELECT COUNT(*) FROM PlaylistSongCrossRef WHERE mediaId = :mediaId")
    suspend fun _getPlaylistContainingSong(mediaId: Long) : Int

    // get All songs from a particular playlist with playlistId
    @Transaction
    @Query("SELECT * FROM Playlist where playlistId = :playlistId")
    suspend fun getAllSongsFromPlaylist(playlistId: Long) : PlaylistWithSongs

    // delete a playlist by playlistId
    @Query("DELETE FROM Playlist WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)
}