package com.example.sih_version_3.authenticationAndChat

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sih_version_3.databinding.ActivityVideoCallBinding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


class videoCall : AppCompatActivity() {
    private lateinit var binding:ActivityVideoCallBinding

    private val appId="a10dbe5e477b450d85f1fef0305604a8"
    private val channelName="aditya"
    private val token ="007eJxTYHh7ntG2vUPa5Y1fT7dJdQ5bd+fqIKkNBSdyCtg3N68yn6LAkGhokJKUappqYm6eZGJqkGJhmmaYlppmYGxgamZgkmgRdYYttSGQkUGIdToLIwMEgvhsDIkpmSWViQwMABF6HeE="
    private val uid=0

    private var isJoined=false
    private var agoraEngine: RtcEngine?=null
    private var localSurfaceView:SurfaceView?=null
    private var remoteSurfaceView:SurfaceView?=null

    private val PERMISSION_ID = 12
    private val REQUESTED_PERMISSION =
        arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA
        )

    private fun checkSelfPermission():Boolean{
        return  !(ContextCompat.checkSelfPermission(
            this,REQUESTED_PERMISSION[0]
        )!=PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                  this,REQUESTED_PERMISSION[1]
                ) !=PackageManager.PERMISSION_GRANTED)
    }

    private fun showMessage(message:String){
        runOnUiThread{
           Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupVideoSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            // By default, the video module is disabled, call enableVideo to enable it.
            agoraEngine!!.enableVideo()
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVideoCallBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if(!checkSelfPermission()){
            ActivityCompat
                .requestPermissions(
                    this,REQUESTED_PERMISSION,PERMISSION_ID
                )
        }

        setupVideoSDKEngine()

        binding.JoinButton.setOnClickListener {
            joinCall()
        }

        binding.LeaveButton.setOnClickListener {
            leaveCall()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        agoraEngine!!.stopPreview()
        agoraEngine!!.leaveChannel()

        Thread{
            RtcEngine.destroy()
            agoraEngine=null
        }.start()
    }

    private fun leaveCall() {

        if(!isJoined){
            showMessage("Join a channel")
        }else{
            agoraEngine!!.leaveChannel()
            showMessage("you left the channel")

            if(remoteSurfaceView!=null) remoteSurfaceView!!.visibility = GONE
            if(localSurfaceView!=null) localSurfaceView!!.visibility = GONE

            isJoined=false
        }
    }

    private fun joinCall() {
        if (checkSelfPermission()){
           val option = ChannelMediaOptions()
            option.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            option.clientRoleType=Constants.CLIENT_ROLE_BROADCASTER

            setupLocalVideo()
            localSurfaceView!!.visibility= VISIBLE
            agoraEngine!!.startPreview()
            agoraEngine!!.joinChannel(token,channelName,uid,option)

        }else{
            Toast.makeText(this,"PERmission not granted",Toast.LENGTH_SHORT).show()
        }
    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the remote host joining the channel to get the uid of the host.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            showMessage("Remote user joined $uid")

            runOnUiThread{ setupRemoteVideo(uid)}

        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
            runOnUiThread {
                remoteSurfaceView!!.visibility = GONE
            }

        }
    }

    private fun setupRemoteVideo(uid:Int){
        remoteSurfaceView= SurfaceView(baseContext)
        remoteSurfaceView!!.setZOrderMediaOverlay(true)
        binding.remoteVideoViewContainer.addView(remoteSurfaceView)

        agoraEngine!!.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                uid
            )
        )
    }
 private fun setupLocalVideo(){
        localSurfaceView= SurfaceView(baseContext)
        binding.remoteVideoViewContainer.addView(localSurfaceView)

        agoraEngine!!.setupLocalVideo(
            VideoCanvas(
                localSurfaceView,
                VideoCanvas.RENDER_MODE_FIT,
                0
            )
        )
    }

}