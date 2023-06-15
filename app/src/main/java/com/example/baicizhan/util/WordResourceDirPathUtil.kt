package com.example.baicizhan.util

import com.example.baicizhan.entity.WordResource
import com.google.gson.Gson
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.ArrayUtils
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import java.util.stream.Collectors

object WordResourceDirPathUtil {
    private const val wordDataFile = "wordData.json"
    private const val usSpeechMp3File = "usSpeech.mp3"
    fun getWordDataFile(wordResourceDir: File?): File {
        return File(wordResourceDir, wordDataFile)
    }

    fun getUsSpeechMp3File(wordResourceDir: File?): File {
        return File(wordResourceDir, usSpeechMp3File)
    }

    private fun getMediaList(wordResourceDir: File): List<File> {
        if (wordResourceDir.isFile) {
            return ArrayList()
        }
        val wordResourceArray = wordResourceDir.listFiles { file: File ->
            ((file.isFile
                    && file.name.endsWith(".jpg")) || (file.name.endsWith(".jpeg")
                    || file.name.endsWith(".png"))
                    || file.name.endsWith(".gif")
                    || file.name.endsWith(".mp4"))
        }
        return if (ArrayUtils.isEmpty(wordResourceArray)) {
            ArrayList()
        } else Arrays.stream(wordResourceArray).collect(Collectors.toList())
    }

    @Throws(IOException::class)
    private fun getAllWordResourceDirByScanWordResourceRootDir(dir: File): List<File> {
        val wordResourceDirList: MutableList<File> = ArrayList(10000)
        Files.walkFileTree(dir.toPath(), object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (wordDataFile == file.toFile().name) {
                    file.toFile().parentFile?.let { wordResourceDirList.add(it) }
                }
                return FileVisitResult.CONTINUE
            }
        })
        return wordResourceDirList
    }

    @Throws(IOException::class)
    fun getWordResourceList(wordResourceRootDir: File): List<WordResource> {
        val wordResourceDirList = getAllWordResourceDirByScanWordResourceRootDir(wordResourceRootDir)
        val wordResourceList = ArrayList<WordResource>()
        for (wordResourceDir in wordResourceDirList) {
            val wordResource : WordResource = Gson().fromJson(getWordDataFile(wordResourceDir).reader(), WordResource::class.java)
            wordResource.wordResourceDir = wordResourceDir.absolutePath
            wordResource.usSpeechFile = getUsSpeechMp3File(wordResourceDir).absolutePath
            val mediaList = getMediaList(wordResourceDir)
            if(CollectionUtils.isNotEmpty(mediaList)){
                wordResource.image = mediaList.sorted()[0].absolutePath
            }
            wordResourceList.add(wordResource)
        }
        return wordResourceList
    }
}