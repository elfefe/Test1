package com.elfefe.test1

import javafx.application.Application
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.geometry.Pos
import javafx.application.Platform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Main : Application() {

    private val layout = GridPane()
    private val play = play()
    private val leftPlayer = player(LEFT_PLAYER)
    private val rightPlayer = player(RIGHT_PLAYER)

    private lateinit var stage: Stage

    private var isCountdownStarted = false

    override fun start(primaryStage: Stage?) {
        primaryStage?.run {
            stage = this
            onCreate()
            scene = Scene(layout, WIDTH, HEIGHT)
            show()
        }
    }

    private fun Stage.onCreate() {
        title = GAME_NAME

        minHeight = HEIGHT
        minWidth = WIDTH

        layout.alignment = Pos.CENTER
        layout.setMinSize(
                width,
                height
        )

        layout.add(play, 1, 0)
        layout.add(leftPlayer, 0, 1)
        layout.add(rightPlayer, 2, 1)

        layout.vgap = (HEIGHT / VERTICAL_DIVIDER)
        layout.hgap = (WIDTH / HORIZONTAL_DIVIDER)

        heightProperty().addListener { _, _, height ->
            layout.vgap = (height as Double / VERTICAL_DIVIDER)
        }

        widthProperty().addListener { _, _, width ->
            layout.hgap = (width as Double / HORIZONTAL_DIVIDER)
        }

        startGame()
    }

    private fun player(player: String): Button = Button(player).apply {
        minWidth = 150.0
        minHeight = 100.0
    }

    private fun play(): Button = Button(PLAY).apply {
        minWidth = 100.0
        minHeight = 80.0
    }

    private fun startGame() {
        play.setOnAction {
            startCountdown()
            play.isDisable = true
            stage.title = POUSSE_LE
        }

        leftPlayer.setOnAction {
            playerWin(LEFT_PLAYER)
        }

        rightPlayer.setOnAction {
            playerWin(RIGHT_PLAYER)
        }
    }

    private fun startCountdown() {
        GlobalScope.launch {
            isCountdownStarted = true
            Platform.runLater {
                leftPlayer.isDisable = true
                rightPlayer.isDisable = true
            }
            for (i in 3 downTo 1) {
                Platform.runLater {
                    play.text = i.toString()
                }
                delay(1000)
            }
            Platform.runLater {
                play.text = GO
                lockMouse()
                leftPlayer.isDisable = false
                rightPlayer.isDisable = false
            }
            isCountdownStarted = false
        }
    }

    private fun lockMouse() {
        try {
            /*
            val securityManager: SecurityManager? = System.getSecurityManager()
            securityManager?.run {
                checkPermission(FXPermissions.CREATE_ROBOT_PERMISSION)
            }

            AccessController.doPrivileged(PrivilegedAction<FXPermission> {
                val mouseXPosition = (Screen.getMainScreen().width - scene.x) + (scene.width / 2)
                val mouseYPosition = (Screen.getMainScreen().height - scene.y) + (scene.height / 2)
                Robot().mouseMove(mouseXPosition, mouseYPosition)
                FXPermissions.CREATE_ROBOT_PERMISSION
            })
            */
        } catch (e: RuntimeException) {
            println("L130 - Exception runtime :$e")
        } catch (i: SecurityException) {
            println("L130 - Exception security :$i")
        }
    }

    private fun playerWin(player: String) {
        stage.title = player + WIN
        play.text = PLAY
        play.isDisable = false
    }

    companion object {
        const val WIDTH = 640.0
        const val HEIGHT = 480.0

        const val VERTICAL_DIVIDER = 3
        const val HORIZONTAL_DIVIDER = 6

        const val LEFT_PLAYER = "Left player"
        const val RIGHT_PLAYER = "Right player"

        const val PLAY = "Play"
        const val POUSSE_LE = "Pousse le !"
        const val GAME_NAME = "Test1"
        const val WIN = " win !"
        const val GO = " Go !"
    }
}