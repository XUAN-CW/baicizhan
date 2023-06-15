package com.example.baicizhan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.baicizhan.database.BaicizhanDatabase
import com.example.baicizhan.entity.WordResource
import com.example.baicizhan.util.PathUtil
import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.ArrayUtils
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var downloadProgressTextView: TextView
    private lateinit var downloadProgressMonitor: TextProgressMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PathUtil.init(filesDir)

        downloadProgressTextView = findViewById(R.id.baicizhan_resource_root_dir)
        downloadProgressTextView.setText(PathUtil.getBaicizhanResourceRootDir().absolutePath)

        findViewById<Button>(R.id.pullGitRepository).setOnClickListener{
            Thread { cloneRepository(PathUtil.cloneUrl,PathUtil.getGitRepositoryDir()) }.start()
        }
        findViewById<Button>(R.id.deleteGitRepository).setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val dialog = builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton(
                    "Delete"
                ) { dialog, which -> // Perform the deletion action here
                    FileUtils.deleteDirectory(PathUtil.getGitRepositoryDir())
                    Toast.makeText(
                        this,
                        "delete " + PathUtil.getGitRepositoryDir(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        }

        findViewById<Button>(R.id.scanWordResource).setOnClickListener{ scanWordResourceDir() }

        val doTodayPlanButton = findViewById<Button>(R.id.doTodayPlan)
        doTodayPlanButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ZhanActivity::class.java)
            startActivity(intent)
        }
    }


    private fun scanWordResourceDir(){
        val wordResourceList = ArrayList<WordResource>()
        for (wordDir in PathUtil.getWordResourceDirList()) {
            Log.i("scanWordResourceDir",wordDir.absolutePath)
            if(PathUtil.getWordDataFile(wordDir.name).exists()){
                val wordResource : WordResource = Gson().fromJson((PathUtil.getWordDataFile(wordDir.name)).reader(), WordResource::class.java)
                val  mediaArray = wordDir.listFiles {file -> file.isFile
                        && (file.name.endsWith(".jpg"))
                        || (file.name.endsWith(".jpeg")
                        || file.name.endsWith(".png"))
                        || file.name.endsWith(".gif")
                        || file.name.endsWith(".mp4") }
                if(ArrayUtils.isNotEmpty(mediaArray)){
                    if (mediaArray != null) {
                        wordResource.image = mediaArray.toList().sorted()[0]?.absolutePath
                    }
                }
                wordResourceList.add(wordResource)
            }
        }
        val baicizhanDatabase = BaicizhanDatabase.getInstance(this)
        for (wordResource in baicizhanDatabase.wordResourceDao().getAllWordResource()) {
            Log.i("check wordResource",wordResource.word)
            baicizhanDatabase.wordResourceDao().delete(wordResource)
        }
        for (wordResource in wordResourceList) {
            try {
                baicizhanDatabase.wordResourceDao().insert(wordResource)
            }catch (e : android.database.sqlite.SQLiteConstraintException){

            }
        }
    }

    private fun cloneRepository(cloneUrl: String,localDir : File) {
        downloadProgressMonitor = object : TextProgressMonitor() {
            override fun onUpdate(taskName: String?, cmp: Int, totalWork: Int, pcnt: Int) {
                super.onUpdate(taskName, cmp, totalWork, pcnt)
                runOnUiThread {
                        downloadProgressTextView.text =  "$taskName, $pcnt% ($cmp/$totalWork) "

                }
            }

            override fun endTask() {
                super.endTask()
                runOnUiThread {
                    downloadProgressTextView.text = "endTask!"
                }

            }
        }

        try {
            if (checkGit(localDir)) {
                // git reset --hard
                val repositoryBuilder = FileRepositoryBuilder().setGitDir(localDir).build()
                val latestCommitId: ObjectId = repositoryBuilder.resolve("HEAD")
                val gitRepository = Git.open(localDir)
                gitRepository.reset().setMode(ResetCommand.ResetType.HARD).setRef(latestCommitId.getName()).call();
                // pull
                val pullCommand: PullCommand = gitRepository
                    .pull().
                    setProgressMonitor(downloadProgressMonitor)
                pullCommand.call()
            }else{
                Git.cloneRepository()
                    .setURI(cloneUrl)
                    .setDirectory(localDir)
                    .setProgressMonitor(downloadProgressMonitor)
                    .call()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun checkGit(gitRepositoryDir : File): Boolean {
        return try {
            val gitRepository = Git.open(gitRepositoryDir)
            gitRepository != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



}